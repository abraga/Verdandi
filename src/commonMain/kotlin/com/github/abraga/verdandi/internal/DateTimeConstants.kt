package com.github.abraga.verdandi.internal

internal object DateTimeConstants {

    const val ZERO: Int = 0

    const val NANOS_PER_MICRO: Int = 1_000

    const val SECONDS_PER_MINUTE: Int = 60
    const val SECONDS_PER_HOUR: Int = 3_600
    const val SECONDS_PER_DAY: Int = 86_400

    const val MINUTES_PER_HOUR: Int = 60
    const val HOURS_PER_DAY: Int = 24
    const val DAYS_PER_WEEK: Int = 7
    const val MONTHS_PER_YEAR: Int = 12

    const val NANOS_PER_MILLI: Long = 1_000_000L
    const val NANOS_PER_SECOND: Long = 1_000_000_000L
    const val NANOS_PER_MINUTE: Long = NANOS_PER_SECOND * SECONDS_PER_MINUTE
    const val NANOS_PER_HOUR: Long = NANOS_PER_SECOND * SECONDS_PER_HOUR
    const val NANOS_PER_DAY: Long = NANOS_PER_SECOND * SECONDS_PER_DAY

    const val MILLIS_PER_SECOND: Long = 1_000L
    const val MILLIS_PER_MINUTE: Long = 60L * MILLIS_PER_SECOND
    const val MILLIS_PER_HOUR: Long = 60L * MILLIS_PER_MINUTE
    const val MILLIS_PER_DAY: Long = 24L * MILLIS_PER_HOUR

    const val DAYS_PER_COMMON_YEAR: Int = 365
    const val DAYS_PER_LEAP_YEAR: Int = 366

    const val EPOCH_OFFSET_DAYS: Long = 719_468L
    const val DAYS_PER_400_YEAR_CYCLE: Long = 146_097L
    const val DAYS_PER_100_YEAR_CYCLE: Int = 36_524
    const val DAYS_PER_4_YEAR_CYCLE: Int = 1_461
    const val THURSDAY_EPOCH_OFFSET: Int = 3

    const val MAX_NANOSECOND: Int = 999_999_999

    const val FIRST_DAY: Int = 1
    const val LAST_DAY_OF_WEEK: Int = 7
    const val LAST_DAY: Int = 31

    const val FIRST_MONTH: Int = 1
    const val LAST_MONTH: Int = 12
    const val LAST_QUARTER: Int = 4

    val MILLISECONDS_RANGE: IntRange = ZERO .. 999
    val SECONDS_RANGE: IntRange = ZERO until SECONDS_PER_MINUTE
    val MINUTES_RANGE: IntRange = ZERO until MINUTES_PER_HOUR
    val HOURS_RANGE: IntRange = ZERO until HOURS_PER_DAY
    val DAYS_RANGE: IntRange = FIRST_DAY..LAST_DAY
    val DAYS_IN_WEEK_RANGE: IntRange = FIRST_DAY..LAST_DAY_OF_WEEK
    val DAYS_IN_YEAR_RANGE: IntRange = FIRST_DAY..DAYS_PER_LEAP_YEAR
    val MONTHS_RANGE: IntRange = FIRST_MONTH..LAST_MONTH
    val QUARTERS_RANGE: IntRange = FIRST_DAY..LAST_QUARTER
    val YEARS_RANGE: IntRange = -MAX_NANOSECOND..MAX_NANOSECOND
}
