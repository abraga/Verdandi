package com.github.abraga.verdandi.internal.format

import kotlin.math.abs

internal fun Int.pad(length: Int = 2, padChar: Char = '0'): String {
    val isNegative = this < 0
    val absolute = abs(this).toString()
    val padded = absolute.padStart(length, padChar)

    return if (isNegative) "-$padded" else padded
}
