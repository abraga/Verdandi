package com.github.abraga.verdandi.api.model

import com.github.abraga.verdandi.Verdandi
import com.github.abraga.verdandi.api.config.VerdandiConfiguration
import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.api.model.duration.DateDuration
import com.github.abraga.verdandi.api.model.duration.DateTimeDuration
import com.github.abraga.verdandi.api.model.relative.VerdandiRelativeMoment
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.adjust.VerdandiAdjustScope
import com.github.abraga.verdandi.internal.format.relative.RelativeTimeResolver
import com.github.abraga.verdandi.api.scope.format.VerdandiFormatScope
import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiFormatPattern
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.adjust.MomentAdjusts
import com.github.abraga.verdandi.internal.duration.MomentDurationResolver
import com.github.abraga.verdandi.internal.extension.currentTimeMillis
import com.github.abraga.verdandi.internal.extension.timeZoneContext
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext
import com.github.abraga.verdandi.internal.factory.query.model.QueryData
import com.github.abraga.verdandi.internal.factory.query.resolveMilliseconds
import com.github.abraga.verdandi.internal.format.VerdandiFormatter
import com.github.abraga.verdandi.internal.format.string.StringPatternFormatter
import kotlinx.serialization.Serializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Represents a specific instant in time with optional timezone context.
 *
 * A [VerdandiMoment] wraps a Unix epoch (milliseconds since `1970-01-01T00:00:00Z`)
 * and exposes rich operations for adjustment, formatting, comparison, and
 * timezone conversion. Instances are created through the [Verdandi] entry point.
 *
 * ### Timezone awareness
 *
 * By default a moment uses the device timezone. Use [inTimeZone] to project
 * the same instant into a different timezone — the epoch stays identical,
 * but [component] values (hour, day, offset, etc.) reflect the target zone:
 *
 * ```kotlin
 * val utc = Verdandi.from("2025-06-15T12:00:00Z")
 * val tokyo = utc inTimeZone VerdandiTimeZone.of("Asia/Tokyo")
 * tokyo.component.hour  // 21
 * utc.inMilliseconds == tokyo.inMilliseconds // true
 * ```
 *
 * ### Adjustments
 *
 * The [adjust] DSL allows natural-language date arithmetic:
 *
 * ```kotlin
 * val tomorrow = moment adjust { add one day }
 * val startOfDay = moment adjust { at startOf day }
 * val shifted = moment adjust { add three hours }
 * ```
 *
 * ### Formatting
 *
 * ```kotlin
 * val iso = moment format { iso.date }       // "2025-06-15"
 * val text = moment format "dd/MM/yyyy HH:mm" // "15/06/2025 14:30"
 * ```
 *
 * ### Equality and comparison
 *
 * Two moments are [equal][equals] when they represent the same instant
 * (same epoch), regardless of timezone context. [compareTo] also uses
 * epoch-only ordering.
 */
@ConsistentCopyVisibility
@Serializable
data class VerdandiMoment internal constructor(
    internal val epoch: Long,
    internal val timeZoneId: String? = null
) : Comparable<VerdandiMoment> {

    internal constructor(
        queryData: QueryData
    ) : this(queryData.timeUnit.resolveMilliseconds(queryData.timeCount, queryData.temporalMoment?.epoch ?: currentTimeMillis()))

    /** The Unix epoch in milliseconds since `1970-01-01T00:00:00Z`. */
    val inMilliseconds: Long
        get() = epoch

    /**
     * Decomposed date/time components (year, month, day, hour, minute, second,
     * millisecond, UTC offset) resolved in this moment's timezone context.
     *
     * The result is lazily computed and cached.
     */
    val component: VerdandiMomentComponent by lazy {
        val context = resolveTimeZoneContext()
        val localDateTime = context.toLocalDateTime(epoch)
        val offset = context.offsetMillisAt(epoch)

        VerdandiMomentComponent(
            years = localDateTime.year,
            months = localDateTime.monthNumber,
            days = localDateTime.dayOfMonth,
            hours = localDateTime.hour,
            minutes = localDateTime.minute,
            seconds = localDateTime.second,
            milliseconds = (localDateTime.nanosecond / NANOS_PER_MILLI).toInt(),
            offsets = offset.toInt()
        )
    }

    override fun toString(): String {
        return buildString {
            with(component) {
                append(year)
                append('-')
                append(month)
                append('-')
                append(day)
                append('T')
                append(hour)
                append(':')
                append(minute)
                append(':')
                append(second)
                append(component.offset)
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VerdandiMoment) return false

        return epoch == other.epoch
    }

    override fun hashCode(): Int {
        return epoch.hashCode()
    }

    override operator fun compareTo(other: VerdandiMoment): Int {
        return this.epoch.compareTo(other.epoch)
    }

    /**
     * Projects this instant into the given [timeZone].
     *
     * The returned moment has the same epoch but its [component] values
     * reflect the target timezone. This is an infix function for readability:
     *
     * ```kotlin
     * val ny = moment inTimeZone VerdandiTimeZone.of("America/New_York")
     * ```
     *
     * @param timeZone the target [VerdandiTimeZone].
     * @return a new [VerdandiMoment] with the same instant in the given timezone.
     */
    infix fun inTimeZone(timeZone: VerdandiTimeZone): VerdandiMoment {
        return VerdandiMoment(epoch, timeZone.id)
    }

    /**
     * Adjusts this moment using the Verdandi DSL.
     *
     * ```kotlin
     * val result = moment adjust {
     *     weekStartsOn = Sunday
     *     add two weeks
     *     at startOf day
     * }
     * ```
     *
     * @param block configuration applied within a [VerdandiAdjustScope].
     * @return a new [VerdandiMoment] reflecting the adjustments.
     */
    infix fun adjust(block: VerdandiAdjustScope.() -> Unit): VerdandiMoment {
        val scope = MomentAdjusts(epoch, resolveTimeZoneContext())

        block(scope)

        return VerdandiMoment(scope.adjustedMilliseconds, scope.timeZone?.id ?: timeZoneId)
    }

    /**
     * Formats this moment using the chain-based DSL.
     *
     * ```kotlin
     * val text = moment format { iso.date }
     * ```
     *
     * @param block a [VerdandiFormatScope] receiver that produces a [VerdandiFormatPattern].
     * @return the formatted string.
     */
    infix fun format(block: VerdandiFormatScope.() -> VerdandiFormatPattern): String {
        return VerdandiFormatter(component, block)
    }

    /**
     * Formats this moment using a pattern string.
     *
     * ```kotlin
     * val text = moment format "yyyy-MM-dd HH:mm"
     * ```
     *
     * See [Verdandi] for the full directive table.
     *
     * @param pattern the format pattern.
     * @return the formatted string.
     */
    infix fun format(pattern: String): String {
        return StringPatternFormatter(component, pattern)
    }

    operator fun compareTo(milliseconds: Long): Int {
        return this.epoch.compareTo(milliseconds)
    }

    /**
     * Creates a [VerdandiInterval] from this moment to [other].
     *
     * The interval is automatically normalized (start ≤ end).
     */
    operator fun rangeTo(other: VerdandiMoment): VerdandiInterval {
        return VerdandiInterval.normalized(VerdandiMoment(epoch, timeZoneId), other)
    }

    operator fun plus(duration: Duration): VerdandiMoment {
        val sum = epoch + duration.inWholeMilliseconds

        return VerdandiMoment(sum, timeZoneId)
    }

    operator fun minus(duration: Duration): VerdandiMoment {
        val difference = epoch - duration.inWholeMilliseconds

        return VerdandiMoment(difference, timeZoneId)
    }

    operator fun plus(dateDuration: DateDuration): VerdandiMoment {
        return operate(
            years = dateDuration.years.toLong(),
            months = dateDuration.months.toLong(),
            weeks = dateDuration.weeks.toLong(),
            days = dateDuration.days.toLong()
        )
    }

    operator fun minus(dateDuration: DateDuration): VerdandiMoment {
        return operate(
            years = -dateDuration.years.toLong(),
            months = -dateDuration.months.toLong(),
            weeks = -dateDuration.weeks.toLong(),
            days = -dateDuration.days.toLong()
        )
    }

    operator fun plus(dateTimeDuration: DateTimeDuration): VerdandiMoment {
        return operate(
            years = dateTimeDuration.years.toLong(),
            months = dateTimeDuration.months.toLong(),
            weeks = dateTimeDuration.weeks.toLong(),
            days = dateTimeDuration.days.toLong(),
            millis = dateTimeDuration.time.inWholeMilliseconds
        )
    }

    operator fun minus(dateTimeDuration: DateTimeDuration): VerdandiMoment {
        return operate(
            years = -dateTimeDuration.years.toLong(),
            months = -dateTimeDuration.months.toLong(),
            weeks = -dateTimeDuration.weeks.toLong(),
            days = -dateTimeDuration.days.toLong(),
            millis = -dateTimeDuration.time.inWholeMilliseconds
        )
    }

    /**
     * Computes the relative description of this moment compared to [Verdandi.now].
     *
     * Returns [Now][com.github.abraga.verdandi.api.model.relative.Now] if the
     * absolute difference is within [nowThreshold], otherwise
     * [Past][com.github.abraga.verdandi.api.model.relative.Past] or
     * [Future][com.github.abraga.verdandi.api.model.relative.Future]
     * with calendar-aware components.
     *
     * ```kotlin
     * val relative = moment.relativeToNow(30.seconds)
     * val text = relative format {
     *     dominant labeled { it.resolve() }
     * }
     * ```
     *
     * @param nowThreshold maximum difference to be considered "now".
     * @return a [VerdandiRelativeMoment] describing the relation.
     */
    fun relativeToNow(nowThreshold: Duration = 59.seconds): VerdandiRelativeMoment {
        return relativeTo(Verdandi.now(), nowThreshold)
    }

    /**
     * Computes the relative description of this moment compared to [other].
     *
     * @param other the reference moment to compare against.
     * @param nowThreshold maximum difference to be considered "now".
     * @return a [VerdandiRelativeMoment] describing the relation.
     * @see relativeToNow
     */
    fun relativeTo(other: VerdandiMoment, nowThreshold: Duration = 59.seconds): VerdandiRelativeMoment {
        return RelativeTimeResolver(nowThreshold).resolve(this, other)
    }

    /**
     * Computes the calendar-aware duration between this moment and [other].
     *
     * The result is always non-negative regardless of argument order.
     *
     * ```kotlin
     * val duration = start.durationUntil(end)
     * // duration.months, duration.weeks, duration.days, duration.hours, etc.
     * ```
     *
     * @param other the target moment.
     * @return a [DateTimeDuration] decomposed into years, months, weeks, days and time.
     */
    fun durationUntil(other: VerdandiMoment): DateTimeDuration {
        return MomentDurationResolver.resolve(this, other)
    }

    /**
     * Returns `true` if this moment is strictly before [other].
     *
     * Comparison is epoch-based and timezone-independent.
     */
    infix fun isBefore(other: VerdandiMoment): Boolean {
        return epoch < other.epoch
    }

    /**
     * Returns `true` if this moment is strictly after [other].
     *
     * Comparison is epoch-based and timezone-independent.
     */
    infix fun isAfter(other: VerdandiMoment): Boolean {
        return epoch > other.epoch
    }

    /**
     * Returns `true` if this moment falls within [interval] (start-inclusive, end-exclusive).
     */
    fun isBetween(interval: VerdandiInterval): Boolean {
        return isBetween(interval.start, interval.end)
    }

    /**
     * Returns `true` if this moment falls within `[start, end)`.
     */
    fun isBetween(start: VerdandiMoment, end: VerdandiMoment): Boolean {
        return epoch >= start.epoch && epoch < end.epoch
    }

    /**
     * Returns `true` if this moment and [other] fall on the same calendar day
     * in their respective timezone contexts.
     */
    infix fun isSameDayAs(other: VerdandiMoment): Boolean {
        val thisComponent = component
        val otherComponent = other.component

        return isSameMonthAs(other) && thisComponent.day == otherComponent.day
    }

    /**
     * Returns `true` if this moment and [other] fall in the same calendar month
     * in their respective timezone contexts.
     */
    infix fun isSameMonthAs(other: VerdandiMoment): Boolean {
        val thisComponent = component
        val otherComponent = other.component

        return isSameYearAs(other) && thisComponent.month == otherComponent.month
    }

    /**
     * Returns `true` if this moment and [other] fall in the same calendar year
     * in their respective timezone contexts.
     */
    infix fun isSameYearAs(other: VerdandiMoment): Boolean {
        return component.year == other.component.year
    }

    /** Returns `true` if this moment falls on today's calendar date. */
    fun isToday(): Boolean {
        return isSameDayAs(Verdandi.now())
    }

    /** Returns `true` if this moment falls on yesterday's calendar date. */
    fun wasYesterday(): Boolean {
        val yesterday = Verdandi.now() adjust { subtract one day }

        return isSameDayAs(yesterday)
    }

    /** Returns `true` if this moment falls on tomorrow's calendar date. */
    fun isTomorrow(): Boolean {
        val tomorrow = Verdandi.now() adjust { add one day }

        return isSameDayAs(tomorrow)
    }

    /** Returns `true` if this moment falls on a Saturday or Sunday. */
    fun isWeekend(): Boolean {
        return component.dayOfWeek.value in 6..7
    }

    /** Returns `true` if this moment falls on a weekday (Monday–Friday). */
    fun isWeekday(): Boolean {
        return isWeekend().not()
    }

    internal fun resolveTimeZoneContext(): TimeZoneContext {
        if (timeZoneId != null) {
            return TimeZoneContext.fromTimeZoneId(timeZoneId)
        }

        val defaultTimeZone = VerdandiConfiguration.get().defaultTimeZone

        if (defaultTimeZone != null) {
            return TimeZoneContext.fromTimeZoneId(defaultTimeZone.id)
        }

        return timeZoneContext
    }

    private fun operate(
        years: Long,
        months: Long,
        weeks: Long,
        days: Long,
        millis: Long = 0L
    ): VerdandiMoment {
        if (years == 0L && months == 0L && weeks == 0L && days == 0L && millis == 0L) {
            return this
        }

        val context = resolveTimeZoneContext()
        val localDateTime = context.toLocalDateTime(epoch)

        var adjusted = localDateTime

        if (years != 0L) {
            adjusted = adjusted.plusYears(years)
        }
        if (months != 0L) {
            adjusted = adjusted.plusMonths(months)
        }
        if (weeks != 0L) {
            adjusted = adjusted.plusWeeks(weeks)
        }
        if (days != 0L) {
            adjusted = adjusted.plusDays(days)
        }
        if (millis != 0L) {
            adjusted = adjusted.plusMillis(millis)
        }

        return VerdandiMoment(context.toEpochMillis(adjusted), timeZoneId)
    }
}
