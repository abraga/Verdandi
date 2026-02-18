package com.github.abraga.verdandi.internal.format.iso

import com.github.abraga.verdandi.internal.format.pad

internal object IsoDateFormatter {

    private const val DATE_SEPARATOR = '-'
    private const val YEAR_PAD_LENGTH = 4

    fun format(year: Int, month: Int, day: Int): String {
        val yearStr = year.pad(YEAR_PAD_LENGTH)
        val monthStr = month.pad()
        val dayStr = day.pad()

        return buildString {
            append(yearStr)
            append(DATE_SEPARATOR)
            append(monthStr)
            append(DATE_SEPARATOR)
            append(dayStr)
        }
    }
}
