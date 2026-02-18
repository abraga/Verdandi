@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.github.abraga.verdandi.internal.adjust

import com.github.abraga.verdandi.api.config.VerdandiConfiguration
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.adjust.AlignmentSelector
import com.github.abraga.verdandi.api.scope.adjust.DateTimeDurationChain
import com.github.abraga.verdandi.api.scope.adjust.VerdandiAdjustScope
import com.github.abraga.verdandi.api.scope.adjust.WeekStart
import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.api.scope.TemporalUnit.Day
import com.github.abraga.verdandi.api.scope.TemporalUnit.Days
import com.github.abraga.verdandi.api.scope.TemporalUnit.Hour
import com.github.abraga.verdandi.api.scope.TemporalUnit.Hours
import com.github.abraga.verdandi.api.scope.TemporalUnit.Millisecond
import com.github.abraga.verdandi.api.scope.TemporalUnit.Milliseconds
import com.github.abraga.verdandi.api.scope.TemporalUnit.Minute
import com.github.abraga.verdandi.api.scope.TemporalUnit.Minutes
import com.github.abraga.verdandi.api.scope.TemporalUnit.Month
import com.github.abraga.verdandi.api.scope.TemporalUnit.Months
import com.github.abraga.verdandi.api.scope.TemporalUnit.Second
import com.github.abraga.verdandi.api.scope.TemporalUnit.Seconds
import com.github.abraga.verdandi.api.scope.TemporalUnit.Week
import com.github.abraga.verdandi.api.scope.TemporalUnit.Weeks
import com.github.abraga.verdandi.api.scope.TemporalUnit.Year
import com.github.abraga.verdandi.api.scope.TemporalUnit.Years
import com.github.abraga.verdandi.api.scope.QuantityUnitSelector
import com.github.abraga.verdandi.internal.DateTimeConstants.DAYS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.HOURS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.MINUTES_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.MONTHS_RANGE
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.DateTimeConstants.SECONDS_RANGE
import com.github.abraga.verdandi.internal.extension.currentLocale
import com.github.abraga.verdandi.internal.extension.verdandiConfigurationError
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext
import com.github.abraga.verdandi.api.model.duration.DateDuration
import com.github.abraga.verdandi.internal.DateTimeConstants.YEARS_RANGE
import com.github.abraga.verdandi.internal.core.grammar.Singular
import com.github.abraga.verdandi.internal.extension.verdandiRequireRangeValidation
import kotlin.time.Duration

internal class MomentAdjusts(
    initialMillis: Long,
    private val context: TimeZoneContext
) : VerdandiAdjustScope {

    private val adjuster = DateTimeAdjuster(initialMillis, context)
    private val dateTimeDurationChain = DateTimeDurationChain(::applyDateDuration, ::applyDuration)
    private var currentOperation: Int = ADD_OPERATION

    val adjustedMilliseconds: Long
        get() = adjuster.adjustedMilliseconds

    override val at: AlignmentSelector
        get() = AlignmentHandler()

    override val add: QuantityUnitSelector<Unit>
        get() = also { currentOperation = ADD_OPERATION }

    override val subtract: QuantityUnitSelector<Unit>
        get() = also { currentOperation = SUBTRACT_OPERATION }

    override var timeZone: VerdandiTimeZone? = null
        set(value) {
            field = value

            val targetId = value?.id ?: context.timeZoneId

            adjuster.switchTimeZone(context.withTimeZoneId(targetId))
        }

    override var weekStartsOn: WeekStart = resolveDefaultWeekStart()

    override fun atYear(value: Int) {
        verdandiRequireRangeValidation(value, YEARS_RANGE)

        adjuster.setYear(value)
    }

    override fun atMonth(value: Int) {
        verdandiRequireRangeValidation(value, MONTHS_RANGE)

        adjuster.setMonth(value)
    }

    override fun atDay(value: Int) {
        verdandiRequireRangeValidation(value, DAYS_RANGE)

        adjuster.setDay(value)
    }

    override fun atHour(value: Int) {
        verdandiRequireRangeValidation(value, HOURS_RANGE)

        adjuster.setHour(value)
    }

    override fun atMinute(value: Int) {
        verdandiRequireRangeValidation(value, MINUTES_RANGE)

        adjuster.setMinute(value)
    }

    override fun atSecond(value: Int) {
        verdandiRequireRangeValidation(value, SECONDS_RANGE)

        adjuster.setSecond(value)
    }

    override fun DateDuration.unaryPlus(): DateTimeDurationChain {
        applyDateDuration(this, true)

        return dateTimeDurationChain
    }

    override fun DateDuration.unaryMinus(): DateTimeDurationChain {
        applyDateDuration(this, false)

        return dateTimeDurationChain
    }

    override fun Duration.unaryPlus(): DateTimeDurationChain {
        applyDuration(this, true)

        return dateTimeDurationChain
    }

    override fun Duration.unaryMinus(): DateTimeDurationChain {
        applyDuration(this, false)

        return dateTimeDurationChain
    }

    override fun build(count: Int, unit: TemporalUnit<*>) {
        when (unit) {
            Millisecond, Milliseconds -> {
                adjuster.addMilliseconds(count * currentOperation)
            }
            Second, Seconds -> {
                adjuster.addSeconds(count * currentOperation)
            }
            Minute, Minutes -> {
                adjuster.addMinutes(count * currentOperation)
            }
            Hour, Hours -> {
                adjuster.addHours(count * currentOperation)
            }
            Day, Days -> {
                adjuster.addDays(count * currentOperation)
            }
            Week, Weeks -> {
                adjuster.addWeeks(count * currentOperation)
            }
            Month, Months -> {
                adjuster.addMonths(count * currentOperation)
            }
            Year, Years -> {
                adjuster.addYears(count * currentOperation)
            }
        }
    }

    private fun applyDateDuration(duration: DateDuration, positive: Boolean) {
        val sign = if (positive) ADD_OPERATION else SUBTRACT_OPERATION

        duration.years.adjustIfNotZero { adjuster.addYears(it * sign) }
        duration.months.adjustIfNotZero { adjuster.addMonths(it * sign) }
        duration.weeks.adjustIfNotZero { adjuster.addWeeks(it * sign) }
        duration.days.adjustIfNotZero { adjuster.addDays(it * sign) }
    }

    private fun applyDuration(duration: Duration, positive: Boolean) {
        val sign = if (positive) ADD_OPERATION else SUBTRACT_OPERATION

        duration.toComponents { longDays, hours, minutes, seconds, nanoseconds ->
            val days = longDays.toInt()
            val millisFromNanos = (nanoseconds / NANOS_PER_MILLI).toInt()

            days.adjustIfNotZero { adjuster.addDays(it * sign) }
            hours.adjustIfNotZero { adjuster.addHours(it * sign) }
            minutes.adjustIfNotZero { adjuster.addMinutes(it * sign) }
            seconds.adjustIfNotZero { adjuster.addSeconds(it * sign) }
            millisFromNanos.adjustIfNotZero { adjuster.addMilliseconds(it * sign) }
        }
    }

    private fun Int.adjustIfNotZero(adjust: (Int) -> Unit) {
        takeIf { it != 0 }?.let { adjust(it) }
    }

    private inner class AlignmentHandler : AlignmentSelector {

        override infix fun startOf(unit: TemporalUnit<Singular>) {
            when (unit) {
                Day -> adjuster.setStartOfDay()
                Week -> adjuster.setStartOfWeek(weekStartsOn.isoDayNumber)
                Month -> adjuster.setStartOfMonth()
                Year -> adjuster.setStartOfYear()
                else -> {
                    verdandiConfigurationError("Invalid temporal unit: ${unit::class.simpleName}")
                }
            }
        }

        override infix fun endOf(unit: TemporalUnit<Singular>) {
            when (unit) {
                Day -> adjuster.setEndOfDay()
                Week -> adjuster.setEndOfWeek(weekStartsOn.isoDayNumber)
                Month -> adjuster.setEndOfMonth()
                Year -> adjuster.setEndOfYear()
                else -> {
                    verdandiConfigurationError(
                        "Invalid temporal unit for alignment: ${unit::class.simpleName}")
                }
            }
        }
    }

    private fun resolveDefaultWeekStart(): WeekStart {
        return VerdandiConfiguration.get().defaultWeekStart
            ?: WeekStart.fromFirstDayOfWeek(currentLocale().firstDayOfWeek)
    }

    companion object {

        private const val ADD_OPERATION = 1
        private const val SUBTRACT_OPERATION = -1
    }
}
