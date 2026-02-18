package com.github.abraga.verdandi.internal.core.unit

import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_PER_WEEK
import com.github.abraga.verdandi.internal.DateTimeConstants.THURSDAY_EPOCH_OFFSET
import com.github.abraga.verdandi.internal.extension.verdandiRequireValidation

internal enum class VerdandiDayOfWeek(val isoDayNumber: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6),
    SUNDAY(7);

    companion object {

        private const val ISO_DAY_OFFSET = 1

        fun of(isoDayNumber: Int): VerdandiDayOfWeek {
            verdandiRequireValidation(isoDayNumber in 1..DAYS_PER_WEEK) {
                "ISO day number must be in 1..7, was $isoDayNumber"
            }

            return entries[isoDayNumber - ISO_DAY_OFFSET]
        }

        fun fromEpochDays(epochDays: Long): VerdandiDayOfWeek {
            val daysSinceEpochMod = (epochDays % DAYS_PER_WEEK).toInt()
            val adjustedForThursday = daysSinceEpochMod + THURSDAY_EPOCH_OFFSET
            val normalizedDayIndex = ((adjustedForThursday % DAYS_PER_WEEK) + DAYS_PER_WEEK) % DAYS_PER_WEEK
            val isoDayNumber = normalizedDayIndex + ISO_DAY_OFFSET

            return entries[isoDayNumber - ISO_DAY_OFFSET]
        }
    }
}
