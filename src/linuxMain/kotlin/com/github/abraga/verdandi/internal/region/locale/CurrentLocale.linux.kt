@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
@file:OptIn(ExperimentalForeignApi::class)

package com.github.abraga.verdandi.internal.region.locale

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import platform.posix.LC_ALL
import platform.posix.getenv
import platform.posix.localeconv
import platform.posix.setlocale

internal actual class CurrentLocale actual constructor(value: String) {

    private val localeId: String by lazy {
        val env = getenv("LC_ALL")?.toKString()
            ?: getenv("LC_MESSAGES")?.toKString()
            ?: getenv("LANG")?.toKString()
            ?: "en_US.UTF-8"

        env.substringBefore('.').substringBefore('@')
    }

    private val language: String
        get() = localeId.substringBefore('_')

    private val country: String
        get() = if ('_' in localeId) localeId.substringAfter('_') else ""

    actual val value: String
        get() = localeId

    actual val displayName: String
        get() = localeId

    actual val nativeDisplayName: String
        get() = localeId

    actual val script: String
        get() = ""

    actual val isRightToLeft: Boolean
        get() = language in RTL_LANGUAGES

    actual val firstDayOfWeek: Int
        get() = 1

    actual val decimalSeparator: String
        get() {
            initLocale()

            return localeconv()?.pointed?.decimal_point?.toKString() ?: "."
        }

    actual val groupingSeparator: String
        get() {
            initLocale()

            return localeconv()?.pointed?.thousands_sep?.toKString() ?: ","
        }

    actual val currencyCode: String
        get() {
            initLocale()

            return localeconv()?.pointed?.int_curr_symbol?.toKString()?.trim() ?: ""
        }

    actual val currencySymbol: String
        get() {
            initLocale()

            return localeconv()?.pointed?.currency_symbol?.toKString() ?: ""
        }

    private fun initLocale() {
        setlocale(LC_ALL, "")
    }

    private companion object {

        private val RTL_LANGUAGES = setOf("ar", "he", "fa", "ur", "yi", "ji")
    }
}
