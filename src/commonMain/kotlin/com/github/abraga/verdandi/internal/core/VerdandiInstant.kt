package com.github.abraga.verdandi.internal.core

import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.core.executor.EpochCalculator
import com.github.abraga.verdandi.internal.core.executor.TemporalParser
import com.github.abraga.verdandi.internal.extension.verdandiRequireParse
import com.github.abraga.verdandi.internal.format.iso.IsoTimestampFormatter
import com.github.abraga.verdandi.internal.util.MathUtils
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@JvmInline
internal value class VerdandiInstant private constructor(
    private val epochMilliseconds: Long
) : Comparable<VerdandiInstant> {

    fun toUtcEpochMillis(): Long {
        return epochMilliseconds
    }

    operator fun plus(duration: Duration): VerdandiInstant {
        val result = MathUtils.addExact(epochMilliseconds, duration.inWholeMilliseconds)

        return VerdandiInstant(result)
    }

    operator fun minus(duration: Duration): VerdandiInstant {
        val result = MathUtils.subtractExact(epochMilliseconds, duration.inWholeMilliseconds)

        return VerdandiInstant(result)
    }

    operator fun minus(other: VerdandiInstant): Duration {
        val result = MathUtils.subtractExact(epochMilliseconds, other.epochMilliseconds)

        return result.milliseconds
    }

    override fun compareTo(other: VerdandiInstant): Int {
        return epochMilliseconds.compareTo(other.epochMilliseconds)
    }

    override fun toString(): String {
        val localDateTime = VerdandiLocalDateTime.fromEpochMillisecondsUtc(epochMilliseconds)

        return IsoTimestampFormatter.formatUtc(localDateTime)
    }

    companion object {

        fun fromUtcEpochMillis(epochMilliseconds: Long): VerdandiInstant {
            return VerdandiInstant(epochMilliseconds)
        }

        fun parse(isoString: String): VerdandiInstant {
            val text = isoString.trim()
            val dateTimeSeparatorIndex = TemporalParser.findDateTimeSeparator(text)

            verdandiRequireParse(dateTimeSeparatorIndex > 0) {
                "Invalid ISO timestamp: $isoString"
            }

            val datePart = text.take(dateTimeSeparatorIndex)
            val timeAndZonePart = text.substring(dateTimeSeparatorIndex + 1)

            val dateComponents = TemporalParser.parseDate(datePart)
            val zoneOffsetResult = TemporalParser.parseTimeWithZone(timeAndZonePart)
            val nanosecond = zoneOffsetResult.timeComponents.nanosecond

            verdandiRequireParse(nanosecond % NANOS_PER_MILLI == 0L) {
                "VerdandiInstant requires millisecond precision; " +
                        "sub-millisecond fractions are not supported: $isoString"
            }

            val epochMillis = EpochCalculator.toEpochMillis(
                year = dateComponents.year,
                month = dateComponents.month,
                day = dateComponents.day,
                hour = zoneOffsetResult.timeComponents.hour,
                minute = zoneOffsetResult.timeComponents.minute,
                second = zoneOffsetResult.timeComponents.second,
                nanosecond = nanosecond
            )

            val adjustedEpochMillis = MathUtils.subtractExact(epochMillis, zoneOffsetResult.offsetMillis)

            return VerdandiInstant(adjustedEpochMillis)
        }
    }
}
