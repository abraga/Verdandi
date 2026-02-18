package com.github.abraga.verdandi.internal.format.iso

import com.github.abraga.verdandi.internal.format.pad

internal object IsoTimeFormatter {

    private const val TIME_SEPARATOR = ':'
    private const val FRACTION_SEPARATOR = '.'
    private const val NANO_PAD_LENGTH = 9

    fun format(hour: Int, minute: Int, second: Int, nanosecond: Int): String {
        val hourStr = hour.pad()
        val minuteStr = minute.pad()
        val secondStr = second.pad()

        val baseTime = buildString {
            append(hourStr)
            append(TIME_SEPARATOR)
            append(minuteStr)
            append(TIME_SEPARATOR)
            append(secondStr)
        }

        if (nanosecond == 0) {
            return baseTime
        }

        val nanoStr = nanosecond.pad(NANO_PAD_LENGTH).trimEnd('0')

        return buildString {
            append(baseTime)
            append(FRACTION_SEPARATOR)
            append(nanoStr)
        }
    }
}
