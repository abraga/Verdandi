package com.github.abraga.verdandi.api.model.relative

import kotlinx.serialization.Serializable

@ConsistentCopyVisibility
@Serializable
data class VerdandiRelativeComponents internal constructor(
    private val years: Int,
    private val months: Int,
    private val weeks: Int,
    private val days: Int,
    private val hours: Int,
    private val minutes: Int,
    private val seconds: Int
) {

    val year: VerdandiRelativeUnit?
        get() {
            if (years == 0) {
                return null
            }

            return resolveUnit(years, singular = Year(), plural = ::Years)
        }

    val month: VerdandiRelativeUnit?
        get() {
            if (months == 0) {
                return null
            }

            return resolveUnit(months, singular = Month(), plural = ::Months)
        }

    val week: VerdandiRelativeUnit?
        get() {
            if (weeks == 0) {
                return null
            }

            return resolveUnit(weeks, singular = Week(), plural = ::Weeks)
        }

    val day: VerdandiRelativeUnit?
        get() {
            if (days == 0) {
                return null
            }

            return resolveUnit(days, singular = Day(), plural = ::Days)
        }

    val hour: VerdandiRelativeUnit?
        get() {
            if (hours == 0) {
                return null
            }

            return resolveUnit(hours, singular = Hour(), plural = ::Hours)
        }

    val minute: VerdandiRelativeUnit?
        get() {
            if (minutes == 0) {
                return null
            }

            return resolveUnit(minutes, singular = Minute(), plural = ::Minutes)
        }

    val second: VerdandiRelativeUnit?
        get() {
            if (seconds == 0) {
                return null
            }

            return resolveUnit(seconds, singular = Second(), plural = ::Seconds)
        }

    val dominant: VerdandiRelativeUnit
        get() = toList().first()

    fun toList(): List<VerdandiRelativeUnit> {
        return listOfNotNull(year, month, week, day, hour, minute, second)
    }

    internal fun format(
        count: Int,
        separator: String,
        lastSeparator: String,
        label: (VerdandiRelativeUnit) -> String
    ): String {
        val parts = toList()
            .take(count)
            .map { "${it.value} ${label(it)}" }

        return when (parts.size) {
            0 -> ""
            1 -> parts.first()
            else -> parts.dropLast(1).joinToString(separator) + lastSeparator + parts.last()
        }
    }

    private fun resolveUnit(
        quantity: Int,
        singular: VerdandiRelativeUnit,
        plural: (Int) -> VerdandiRelativeUnit
    ): VerdandiRelativeUnit {
        if (quantity == 1) {
            return singular
        }

        return plural(quantity)
    }
}
