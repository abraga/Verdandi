package com.github.abraga.verdandi.internal.factory

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.factory.VerdandiIntervalFactoryScope
import com.github.abraga.verdandi.internal.factory.parse.InputResolver
import com.github.abraga.verdandi.internal.factory.query.IntervalFactory
import com.github.abraga.verdandi.internal.format.string.StringPatternParser
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext

internal class MomentFactory {

    private val resolver: InputResolver = InputResolver()

    fun fromMilliseconds(milliseconds: Long?): VerdandiMoment {
        val epochMilliseconds = resolver.resolveMilliseconds(milliseconds)

        return VerdandiMoment(epochMilliseconds)
    }

    fun fromTimestamp(timestamp: String): VerdandiMoment {
        val epochMilliseconds = resolver.resolveTimestamp(timestamp)

        return VerdandiMoment(epochMilliseconds)
    }

    fun fromComponents(
        year: Int,
        month: Int,
        day: Int,
        hour: Int,
        minute: Int,
        second: Int,
        millisecond: Int
    ): VerdandiMoment {
        val epochMilliseconds = resolver.resolveComponents(
            years = year,
            months = month,
            days = day,
            hours = hour,
            minutes = minute,
            seconds = second,
            milliseconds = millisecond
        )

        return VerdandiMoment(epochMilliseconds)
    }

    fun fromRange(startEpochMilliseconds: Long, endEpochMilliseconds: Long): VerdandiInterval {
        val start = fromMilliseconds(startEpochMilliseconds)
        val end = fromMilliseconds(endEpochMilliseconds)

        return VerdandiInterval.normalized(start, end)
    }

    fun fromRange(range: LongRange): VerdandiInterval {
        val start = fromMilliseconds(range.first)

        val endExclusive = if (range.last == Long.MAX_VALUE) {
            Long.MAX_VALUE
        } else {
            range.last + 1
        }

        val end = fromMilliseconds(endExclusive)

        return VerdandiInterval.normalized(start, end)
    }

    fun fromTimestampRange(startTimestamp: String, endTimestamp: String): VerdandiInterval {
        val start = fromTimestamp(startTimestamp)
        val end = fromTimestamp(endTimestamp)

        return VerdandiInterval.normalized(start, end)
    }

    fun fromPattern(input: String, pattern: String): VerdandiMoment {
        return StringPatternParser(input, pattern)
    }

    fun fromPattern(input: String, pattern: String, context: TimeZoneContext): VerdandiMoment {
        return StringPatternParser(input, pattern, context)
    }

    fun fromIntervalQuery(block: VerdandiIntervalFactoryScope.() -> VerdandiInterval): VerdandiInterval {
        val scope = IntervalFactory()

        return scope.block()
    }
}
