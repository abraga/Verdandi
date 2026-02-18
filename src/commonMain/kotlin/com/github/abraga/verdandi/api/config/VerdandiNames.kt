package com.github.abraga.verdandi.api.config

import com.github.abraga.verdandi.api.model.component.DayOfWeek
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.internal.extension.currentLocale
import com.github.abraga.verdandi.internal.factory.component.platform.getAmPmName
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale
import kotlinx.serialization.Serializable

/**
 * VerdandiNames - Configuration for localized names
 *
 * This class holds the localized names for months, weekdays, and AM/PM indicators.
 * It is used by Verdandi to provide locale-aware formatting and parsing of dates and times.
 *
 * @property monthNames List of month names (e.g., January, February, etc.)
 * @property weekdayNames List of weekday names (e.g., Monday, Tuesday, etc.)
 * @property amPmNames Pair of AM and PM names (e.g., "AM" and "PM")
 */
@ConsistentCopyVisibility
@Serializable
data class VerdandiNames private constructor(
    val monthNames: List<String>,
    val weekdayNames: List<String>,
    val amPmNames: Pair<String, String>
) {

    companion object {

        operator fun invoke(): VerdandiNames {
            val monthsNames = (1..12).map { Month(it).name }
            val weekdayNames = (1..7).map { DayOfWeek(it).name }
            val amPm = getAmPmName(currentLocale())

            return VerdandiNames(
                monthNames = monthsNames,
                weekdayNames = weekdayNames,
                amPmNames = amPm
            )
        }
    }
}
