package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.config.Configuration
import com.github.abraga.verdandi.api.exception.VerdandiValidationException
import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.factory.VerdandiIntervalFactoryScope
import com.github.abraga.verdandi.api.scope.recurrence.VerdandiRecurrenceScope
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_SECOND
import com.github.abraga.verdandi.internal.extension.verdandiInputError
import com.github.abraga.verdandi.internal.core.VerdandiLocalDate
import com.github.abraga.verdandi.internal.core.VerdandiLocalTime
import com.github.abraga.verdandi.internal.factory.MomentFactory
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext

/**
 * Main entry point for the Verdandi date and time library.
 *
 * Verdandi provides a fluent, DSL-driven API for creating, formatting, adjusting,
 * and comparing date/time values across Kotlin Multiplatform targets.
 *
 * ### Creating moments
 *
 * ```kotlin
 * val now = Verdandi.now()
 * val explicit = Verdandi.at(2025, 6, 15, 14, 30)
 * val fromEpoch = Verdandi.from(1750000000000L)
 * val fromIso = Verdandi.from("2025-06-15T14:30:00Z")
 * val parsed = Verdandi.parse("15/06/2025", "dd/MM/yyyy")
 * ```
 *
 * ### Timezone conversion
 *
 * A [VerdandiMoment] can be projected into any IANA timezone without
 * changing the underlying instant:
 *
 * ```kotlin
 * val utc = Verdandi.from("2025-06-15T12:00:00Z")
 * val tokyo = utc inTimeZone VerdandiTimeZone.of("Asia/Tokyo")
 * tokyo.component.hour // 21
 * ```
 *
 * ### Adjusting moments
 *
 * ```kotlin
 * val tomorrow = Verdandi.now() adjust { add one day }
 * val startOfWk = Verdandi.now() adjust { at startOf week }
 * ```
 *
 * ### Formatting
 *
 * ```kotlin
 * val iso = moment format { iso.date }
 * val text = moment format "yyyy-MM-dd HH:mm"
 * ```
 *
 * ### Creating intervals
 *
 * ```kotlin
 * val recent = Verdandi.interval { last thirty days }
 * val custom = Verdandi.interval("2025-01-01T00:00:00Z", "2025-12-31T23:59:59Z")
 * val range = Verdandi.interval(startMs..endMs)
 * ```
 *
 * ### Validation
 *
 * ```kotlin
 * Verdandi.isValidDate(2024, 2, 29) // true  (leap year)
 * Verdandi.isValidDate(2025, 2, 29) // false
 * Verdandi.isValidTime(23, 59, 59)  // true
 * ```
 *
 * ### Pattern directives
 *
 * Supported directives for [parse] and [VerdandiMoment.format]:
 *
 * | Directive | Description              | Example          |
 * |-----------|--------------------------|------------------|
 * | `yyyy`    | Four-digit year          | `2025`           |
 * | `yy`      | Two-digit year (2000s)   | `25`             |
 * | `MMMM`    | Full month name          | `January`        |
 * | `MMM`     | Abbreviated month name   | `Jan`            |
 * | `MM`      | Zero-padded month        | `01`             |
 * | `dd`      | Zero-padded day          | `09`             |
 * | `d`       | Day (variable length)    | `9`              |
 * | `EEEE`    | Full weekday name        | `Monday`         |
 * | `EEE`     | Abbreviated weekday name | `Mon`            |
 * | `HH`      | Hour 0–23 (zero-padded)  | `14`             |
 * | `hh`      | Hour 1–12 (zero-padded)  | `02`             |
 * | `mm`      | Minute (zero-padded)     | `05`             |
 * | `ss`      | Second (zero-padded)     | `09`             |
 * | `SSS`     | Millisecond              | `456`            |
 * | `a`       | AM/PM marker             | `PM`             |
 * | `Q`       | Quarter                  | `2`              |
 * | `Z`       | UTC offset               | `+09:00` or `Z`  |
 *
 * Literal text can be escaped with single quotes: `'at'` outputs the word `at`.
 */
object Verdandi {

    private val momentFactory: MomentFactory by lazy {
        MomentFactory()
    }

    val config = Configuration

    /**
     * Shorthand for [now]. Allows `Verdandi()` syntax.
     *
     * @return a [VerdandiMoment] representing the current instant.
     */
    operator fun invoke(): VerdandiMoment {
        return now()
    }

    /**
     * Returns the current instant as a [VerdandiMoment].
     *
     * @return a [VerdandiMoment] anchored to the system clock at the time of invocation.
     */
    fun now(): VerdandiMoment {
        return momentFactory.fromMilliseconds(null)
    }

    /**
     * Creates a [VerdandiMoment] from individual date and time components
     * interpreted in the device's default timezone.
     *
     * @param year        the calendar year (e.g. `2025`).
     * @param month       the month of the year, 1–12.
     * @param day         the day of the month, 1–28/29/30/31 depending on [month] and [year].
     * @param hour        the hour of the day, 0–23. Defaults to `0`.
     * @param minute      the minute of the hour, 0–59. Defaults to `0`.
     * @param second      the second of the minute, 0–59. Defaults to `0`.
     * @param millisecond the millisecond of the second, 0–999. Defaults to `0`.
     * @return a [VerdandiMoment] corresponding to the specified wall-clock time.
     * @throws com.github.abraga.verdandi.api.exception.VerdandiValidationException
     *   if any component is out of range (e.g. day 31 for a 30-day month).
     */
    fun at(
        year: Int,
        month: Int,
        day: Int,
        hour: Int = 0,
        minute: Int = 0,
        second: Int = 0,
        millisecond: Int = 0
    ): VerdandiMoment {
        return momentFactory.fromComponents(
            year = year,
            month = month,
            day = day,
            hour = hour,
            minute = minute,
            second = second,
            millisecond = millisecond
        )
    }

    /**
     * Creates a [VerdandiMoment] from a Unix epoch timestamp.
     *
     * @param epochMilliseconds milliseconds since `1970-01-01T00:00:00Z`.
     * @return a [VerdandiMoment] representing the given instant.
     */
    fun from(epochMilliseconds: Long): VerdandiMoment {
        return momentFactory.fromMilliseconds(epochMilliseconds)
    }

    /**
     * Creates a [VerdandiMoment] from an ISO-8601 timestamp string.
     *
     * Accepted formats include `2025-06-15T14:30:00Z` and
     * `2025-06-15T14:30:00+09:00`.
     *
     * **Timestamps without an explicit offset or zone designator are
     * interpreted as UTC**, not as the device's local timezone. This differs
     * from [at], which uses the device timezone. To parse a local timestamp
     * in a specific timezone, use [parse] with an explicit [VerdandiTimeZone].
     *
     * @param timestamp an ISO-8601 formatted string.
     * @return a [VerdandiMoment] representing the parsed instant.
     * @throws com.github.abraga.verdandi.api.exception.VerdandiParseException
     *   if [timestamp] is not a valid ISO-8601 string.
     */
    fun from(timestamp: String): VerdandiMoment {
        return momentFactory.fromTimestamp(timestamp)
    }

    /**
     * Creates a [VerdandiMoment] from a Unix epoch timestamp expressed in seconds.
     *
     * The provided value is interpreted as the number of seconds elapsed since
     * `1970-01-01T00:00:00Z` (Unix epoch) and is internally converted to milliseconds
     * before creating the [VerdandiMoment].
     *
     * @param seconds seconds since `1970-01-01T00:00:00Z`.
     * @return a [VerdandiMoment] representing the given instant.
     */
    fun fromSeconds(seconds: Long): VerdandiMoment {
        val epochMilliseconds = seconds * MILLIS_PER_SECOND

        if (seconds != 0L && epochMilliseconds / seconds != MILLIS_PER_SECOND) {
            verdandiInputError(
                "Seconds value causes overflow when converting to milliseconds: $seconds"
            )
        }

        return from(epochMilliseconds)
    }

    /**
     * Parses a date/time string using a custom pattern and interprets it
     * in the device's default timezone.
     *
     * ```kotlin
     * val moment = Verdandi.parse("15-Jun-2025 14:30", "dd-MMM-yyyy HH:mm")
     * ```
     *
     * When the pattern contains a `Z` directive, the offset embedded in
     * [input] takes precedence over the device timezone.
     *
     * @param input   the date/time string to parse.
     * @param pattern the format pattern (see class-level table for directives).
     * @return a [VerdandiMoment] representing the parsed instant.
     * @throws com.github.abraga.verdandi.api.exception.VerdandiParseException
     *   if [input] does not match [pattern] or contains invalid values.
     * @see parse overload with explicit [VerdandiTimeZone].
     */
    fun parse(input: String, pattern: String): VerdandiMoment {
        return momentFactory.fromPattern(input, pattern)
    }

    /**
     * Parses a date/time string using a custom pattern and interprets it
     * in the given [timeZone].
     *
     * ```kotlin
     * val tokyo = VerdandiTimeZone.of("Asia/Tokyo")
     * val moment = Verdandi.parse("2025-06-15 14:30", "yyyy-MM-dd HH:mm", tokyo)
     * // moment represents 14:30 JST → 05:30 UTC
     * ```
     *
     * @param input    the date/time string to parse.
     * @param pattern  the format pattern (see class-level table for directives).
     * @param timeZone the [VerdandiTimeZone] in which to interpret [input].
     * @return a [VerdandiMoment] whose timezone context is set to [timeZone].
     * @throws com.github.abraga.verdandi.api.exception.VerdandiParseException
     *   if [input] does not match [pattern] or contains invalid values.
     */
    fun parse(input: String, pattern: String, timeZone: VerdandiTimeZone): VerdandiMoment {
        return momentFactory.fromPattern(input, pattern, TimeZoneContext.fromTimeZoneId(timeZone.id))
    }

    /**
     * Creates a [VerdandiInterval] `[start, end)` from two Unix epoch timestamps.
     *
     * The resulting interval is half-open and automatically normalized so that
     * [VerdandiInterval.start] ≤ [VerdandiInterval.end].
     *
     * @param startEpochMilliseconds epoch milliseconds for one bound (inclusive).
     * @param endEpochMilliseconds   epoch milliseconds for the other bound (exclusive).
     * @return a normalized [VerdandiInterval].
     */
    fun interval(startEpochMilliseconds: Long, endEpochMilliseconds: Long): VerdandiInterval {
        return momentFactory.fromRange(startEpochMilliseconds, endEpochMilliseconds)
    }

    /**
     * Creates a [VerdandiInterval] from two ISO-8601 timestamp strings.
     *
     * @param startTimestamp an ISO-8601 string for one bound.
     * @param endTimestamp   an ISO-8601 string for the other bound.
     * @return a normalized [VerdandiInterval].
     * @throws com.github.abraga.verdandi.api.exception.VerdandiParseException
     *   if either timestamp is not a valid ISO-8601 string.
     */
    fun interval(startTimestamp: String, endTimestamp: String): VerdandiInterval {
        return momentFactory.fromTimestampRange(startTimestamp, endTimestamp)
    }

    /**
     * Creates a [VerdandiInterval] from a [LongRange] of epoch milliseconds.
     *
     * Because [LongRange] is inclusive on both ends, the resulting half-open
     * interval uses `range.last + 1` as its exclusive upper bound.
     *
     * @param range a [LongRange] whose [LongRange.first] and [LongRange.last]
     *   represent epoch milliseconds (both inclusive).
     * @return a normalized [VerdandiInterval].
     */
    fun interval(range: LongRange): VerdandiInterval {
        return momentFactory.fromRange(range)
    }

    /**
     * Creates a [VerdandiInterval] using the natural-language DSL.
     *
     * ```kotlin
     * val recent = Verdandi.interval { last thirty days }
     * val future = Verdandi.interval { next two weeks }
     * ```
     *
     * @param block a [VerdandiIntervalFactoryScope] receiver that evaluates to a [VerdandiInterval].
     * @return the [VerdandiInterval] produced by [block].
     */
    fun interval(block: VerdandiIntervalFactoryScope.() -> VerdandiInterval): VerdandiInterval {
        return momentFactory.fromIntervalQuery(block)
    }

    /**
     * Checks whether the given year, month, and day form a valid calendar date.
     *
     * ```kotlin
     * Verdandi.isValidDate(2024, 2, 29) // true  — leap year
     * Verdandi.isValidDate(2025, 2, 29) // false
     * ```
     *
     * @param year  the calendar year.
     * @param month the month, 1–12.
     * @param day   the day, 1–31.
     * @return `true` if the combination represents a real calendar date.
     */
    fun isValidDate(year: Int, month: Int, day: Int): Boolean {
        return try {
            VerdandiLocalDate.of(year, month, day)
            true
        } catch (_: VerdandiValidationException) {
            false
        }
    }

    /**
     * Checks whether the given time components form a valid time of day.
     *
     * ```kotlin
     * Verdandi.isValidTime(23, 59, 59)     // true
     * Verdandi.isValidTime(24, 0, 0)       // false
     * Verdandi.isValidTime(12, 30, 0, 999) // true
     * ```
     *
     * @param hour        the hour, 0–23.
     * @param minute      the minute, 0–59.
     * @param second      the second, 0–59. Defaults to `0`.
     * @param millisecond the millisecond, 0–999. Defaults to `0`.
     * @return `true` if the combination represents a valid time of day.
     */
    fun isValidTime(hour: Int, minute: Int, second: Int = 0, millisecond: Int = 0): Boolean {
        return try {
            VerdandiLocalTime.of(hour, minute, second, millisecond * 1_000_000)
            true
        } catch (_: VerdandiValidationException) {
            false
        }
    }

    /**
     * Creates a [VerdandiRecurrenceMoments] using the natural-language DSL, anchored to
     * the current moment.
     *
     * ```kotlin
     * val upcomingFridays = Verdandi.recurrence {
     *     every week on fridays indefinitely
     * }
     * ```
     *
     * @param block a [VerdandiRecurrenceScope] receiver that evaluates to a [VerdandiRecurrenceMoments].
     * @return the [VerdandiRecurrenceMoments] produced by [block].
     */
    fun recurrence(
        limit: Int? = null,
        block: VerdandiRecurrenceScope.() -> VerdandiRecurrenceMoments
    ): VerdandiRecurrenceMoments {
        return recurrence(now(), limit, block)
    }

    /**
     * Creates a [VerdandiRecurrenceMoments] using the natural-language DSL, anchored to
     * the specified [moment].
     *
     * ```kotlin
     * val upcomingFridays = Verdandi.recurrence(Verdandi.now()) {
     *     every week on fridays indefinitely
     * }
     * ```
     *
     * @param moment the [VerdandiMoment] that serves as the starting point for the recurrence.
     * @param block   a [VerdandiRecurrenceScope] receiver that evaluates to a [VerdandiRecurrenceMoments].
     * @return the [VerdandiRecurrenceMoments] produced by [block].
     */
    fun recurrence(
        moment: VerdandiMoment,
        limit: Int? = null,
        block: VerdandiRecurrenceScope.() -> VerdandiRecurrenceMoments
    ): VerdandiRecurrenceMoments {
        val scope = VerdandiRecurrenceScope(moment, limit)

        return scope.block()
    }
}
