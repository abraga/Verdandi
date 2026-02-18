package com.github.abraga.verdandi.api.scope.adjust

import kotlinx.serialization.Serializable

@Serializable
sealed class WeekStart(internal val isoDayNumber: Int) {

    companion object {

        private const val SUNDAY_FIRST_DAY = 0
        private const val SATURDAY_FIRST_DAY = 6

        internal fun fromFirstDayOfWeek(firstDayOfWeek: Int): WeekStart {
            return when (firstDayOfWeek) {
                SUNDAY_FIRST_DAY -> Sunday
                SATURDAY_FIRST_DAY -> Saturday
                else -> Monday
            }
        }
    }
}

@Serializable
data object Monday : WeekStart(1)

@Serializable
data object Saturday : WeekStart(6)

@Serializable
data object Sunday : WeekStart(7)
