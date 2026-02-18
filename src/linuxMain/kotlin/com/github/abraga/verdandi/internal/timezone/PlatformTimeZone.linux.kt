@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalForeignApi::class)

package com.github.abraga.verdandi.internal.timezone

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.S_IFDIR
import platform.posix.S_IFMT
import platform.posix.access
import platform.posix.closedir
import platform.posix.F_OK
import platform.posix.getenv
import platform.posix.localtime_r
import platform.posix.opendir
import platform.posix.readdir
import platform.posix.readlink
import platform.posix.setenv
import platform.posix.stat
import platform.posix.tm
import platform.posix.tzset
import platform.posix.unsetenv

internal actual object PlatformTimeZone {

    private const val MILLIS_PER_SECOND = 1000L
    private const val ZONEINFO_PATH = "/usr/share/zoneinfo/"

    actual fun offsetMillisAt(timeZoneId: String, epochMillis: Long): Long {
        return memScoped {
            val originalTz = getenv("TZ")?.toKString()

            setenv("TZ", ":$timeZoneId", 1)
            tzset()

            try {
                val epochSeconds = epochMillis / MILLIS_PER_SECOND
                val localTm = alloc<tm>()

                longArrayOf(epochSeconds).usePinned { pinned ->
                    localtime_r(pinned.addressOf(0).reinterpret(), localTm.ptr)
                }

                localTm.tm_gmtoff * MILLIS_PER_SECOND
            } finally {
                if (originalTz != null) {
                    setenv("TZ", originalTz, 1)
                } else {
                    unsetenv("TZ")
                }
                tzset()
            }
        }
    }

    actual fun isTimeZoneIdValid(timeZoneId: String): Boolean {
        return access("$ZONEINFO_PATH$timeZoneId", F_OK) == 0
    }

    actual fun defaultId(): String {
        val tzEnv = getenv("TZ")?.toKString()

        if (tzEnv != null) {
            val id = if (tzEnv.startsWith(":")) tzEnv.substring(1) else tzEnv

            if (id.isNotEmpty() && isTimeZoneIdValid(id)) return id
        }

        return readLocalTimeLink() ?: "UTC"
    }

    actual fun availableIds(): Set<String> {
        return enumerateZoneInfo(ZONEINFO_PATH, "")
    }

    private fun readLocalTimeLink(): String? = memScoped {
        val buf = allocArray<ByteVar>(512)
        val len = readlink("/etc/localtime", buf, 512u)

        if (len <= 0) return null

        val target = buf.toKString()

        if (target.startsWith(ZONEINFO_PATH)) target.removePrefix(ZONEINFO_PATH) else null
    }

    private fun enumerateZoneInfo(basePath: String, relativePath: String): Set<String> {
        val result = mutableSetOf<String>()
        val dirPath = "$basePath$relativePath"
        val dir = opendir(dirPath) ?: return result

        try {
            while (true) {
                val entry = readdir(dir) ?: break
                val name = entry.pointed.d_name.toKString()

                if (name == "." || name == ".." || name == "posix" || name == "right") continue

                val fullRelative = if (relativePath.isEmpty()) name else "$relativePath/$name"
                val fullPath = "$basePath$fullRelative"

                val isDirectory = memScoped {
                    val s = alloc<stat>()
                    stat(fullPath, s.ptr)
                    (s.st_mode.toInt() and S_IFMT) == S_IFDIR
                }

                if (isDirectory) {
                    result.addAll(enumerateZoneInfo(basePath, fullRelative))
                } else if ('.' !in name) {
                    result.add(fullRelative)
                }
            }
        } finally {
            closedir(dir)
        }

        return result
    }
}
