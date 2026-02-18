package com.github.abraga.verdandi.api.model.component

import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_IN_WEEK_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_IN_YEAR_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.HOURS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLISECONDS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.MINUTES_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MINUTES_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.QUARTERS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.SECONDS_RANGE
import com.github.abraga.verdandi.internal.core.unit.VerdandiMonth
import com.github.abraga.verdandi.internal.extension.capitalized
import com.github.abraga.verdandi.internal.extension.currentLocale
import com.github.abraga.verdandi.internal.extension.verdandiRequireRangeValidation
import com.github.abraga.verdandi.internal.factory.component.platform.getDayOfWeekName
import com.github.abraga.verdandi.internal.factory.component.platform.getMonthName
import com.github.abraga.verdandi.internal.format.pad
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline
import kotlin.math.abs

@Serializable
sealed interface VerdandiComponent : Comparable<VerdandiComponent> {

    val value: Number

    override fun compareTo(other: VerdandiComponent): Int {
        if (value is Long || other.value is Long) {
            return (value.toLong()).compareTo(other.value.toLong())
        }

        return (value.toInt()).compareTo(other.value.toInt())
    }
}

@JvmInline
@Serializable
value class Year internal constructor(override val value: Int) : VerdandiComponent {

    val short: String
        get() = value.toString().takeLast(2)

    val isLeapYear: Boolean
        get() = VerdandiMonth.isLeapYear(value)

    override fun toString(): String {
        return value.pad(4)
    }
}

@JvmInline
@Serializable
value class Quarter internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, QUARTERS_RANGE)
    }

    val name: String
        get() = "Q$value"

    override fun toString(): String {
        return "$value"
    }
}

@JvmInline
@Serializable
value class Month internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, MONTHS_RANGE)
    }

    val name: String
        get() {
            val locale = currentLocale()

            return getMonthName(locale).capitalized()
        }

    val shortName: String
        get() = name.take(3)

    val quarter: Quarter
        get() = Quarter((value - 1) / 3 + 1)

    fun lengthInYear(year: Int): Int {
        return VerdandiMonth.of(value).lengthInYear(year)
    }

    override fun toString(): String {
        return value.pad()
    }
}

@JvmInline
@Serializable
value class DayOfMonth internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, DAYS_RANGE)
    }

    override fun toString(): String {
        return value.pad()
    }
}

@JvmInline
@Serializable
value class DayOfWeek internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, DAYS_IN_WEEK_RANGE)
    }

    val name: String
        get() {
            val locale = currentLocale()

            return getDayOfWeekName(locale).lowercase()
        }

    val shortName: String
        get() = name.take(3)

    override fun toString(): String {
        return "$value"
    }
}

@JvmInline
@Serializable
value class DayOfTheYear internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, DAYS_IN_YEAR_RANGE)
    }

    override fun toString(): String {
        return "$value"
    }
}

@JvmInline
@Serializable
value class Hour internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, HOURS_RANGE)
    }

    val valueIn12: Int
        get() = when {
            value == 0 -> 12
            value > 12 -> value - 12
            else -> value
        }

    val isAM: Boolean
        get() = value < 12

    val isPM: Boolean
        get() = value >= 12

    override fun toString(): String {
        return value.pad()
    }
}

@JvmInline
@Serializable
value class Minute internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, MINUTES_RANGE)
    }

    override fun toString(): String {
        return value.pad()
    }
}

@JvmInline
@Serializable
value class Second internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, SECONDS_RANGE)
    }

    override fun toString(): String {
        return value.pad()
    }
}

@JvmInline
@Serializable
value class Millisecond internal constructor(override val value: Int) : VerdandiComponent {

    init {
        verdandiRequireRangeValidation(value, MILLISECONDS_RANGE)
    }

    override fun toString(): String {
        return value.pad()
    }
}

@JvmInline
@Serializable
value class Offset internal constructor(override val value: Int) : VerdandiComponent {

    override fun toString(): String {
        val sign = if (value >= 0) "+" else "-"
        val totalMinutes = abs((value / MILLIS_PER_MINUTE).toInt())
        val hours = totalMinutes / MINUTES_PER_HOUR
        val minutes = totalMinutes % MINUTES_PER_HOUR

        return "$sign${hours.pad()}:${minutes.pad()}"
    }
}
