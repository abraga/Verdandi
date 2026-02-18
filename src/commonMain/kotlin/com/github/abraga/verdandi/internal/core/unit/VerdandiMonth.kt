package com.github.abraga.verdandi.internal.core.unit

import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_RANGE
import com.github.abraga.verdandi.internal.extension.verdandiRequireRangeValidation

internal enum class VerdandiMonth(val number: Int, private val baseDays: Int) {
    JANUARY(1, 31),
    FEBRUARY(2, 28),
    MARCH(3, 31),
    APRIL(4, 30),
    MAY(5, 31),
    JUNE(6, 30),
    JULY(7, 31),
    AUGUST(8, 31),
    SEPTEMBER(9, 30),
    OCTOBER(10, 31),
    NOVEMBER(11, 30),
    DECEMBER(12, 31);

    fun lengthInYear(year: Int): Int {
        if (this == FEBRUARY && isLeapYear(year)) {
            return FEBRUARY_LEAP_DAYS
        }

        return baseDays
    }

    companion object {

        private const val FEBRUARY_LEAP_DAYS = 29

        private const val LEAP_YEAR_CYCLE = 4
        private const val CENTURY_CYCLE = 100
        private const val LEAP_CENTURY_CYCLE = 400

        fun of(number: Int): VerdandiMonth {
            verdandiRequireRangeValidation(number, MONTHS_RANGE)

            return entries[number - 1]
        }

        fun isLeapYear(year: Int): Boolean {
            val divisibleBy4 = year % LEAP_YEAR_CYCLE == 0
            val divisibleBy100 = year % CENTURY_CYCLE == 0
            val divisibleBy400 = year % LEAP_CENTURY_CYCLE == 0

            return divisibleBy4 && (divisibleBy100.not() || divisibleBy400)
        }
    }
}
