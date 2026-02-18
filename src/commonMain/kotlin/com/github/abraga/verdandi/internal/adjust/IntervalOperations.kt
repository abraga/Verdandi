package com.github.abraga.verdandi.internal.adjust

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.adjust.VerdandiIntervalOperationScope
import com.github.abraga.verdandi.internal.duration.MomentDurationResolver
import com.github.abraga.verdandi.internal.util.MathUtils
import com.github.abraga.verdandi.api.model.duration.DateTimeDuration
import kotlin.time.Duration

internal class IntervalOperations(
    private val start: VerdandiMoment,
    private val end: VerdandiMoment
) : VerdandiIntervalOperationScope {

    private val startMillis: Long
        get() = start.epoch

    private val endMillis: Long
        get() = end.epoch

    private val timeZoneId: String?
        get() = start.timeZoneId

    override operator fun plus(duration: Duration): VerdandiInterval {
        val delta = duration.inWholeMilliseconds

        return VerdandiInterval.normalized(
            start = VerdandiMoment(startMillis + delta, timeZoneId),
            end = VerdandiMoment(endMillis + delta, timeZoneId)
        )
    }

    override operator fun minus(duration: Duration): VerdandiInterval {
        return plus(-duration)
    }

    override fun expand(duration: Duration): VerdandiInterval {
        val delta = duration.inWholeMilliseconds

        return VerdandiInterval.normalized(
            start = VerdandiMoment(startMillis - delta, timeZoneId),
            end = VerdandiMoment(endMillis + delta, timeZoneId)
        )
    }

    override fun shrink(duration: Duration): VerdandiInterval {
        val (newStart, newEnd) = MathUtils.shrinkInterval(
            startMillis,
            endMillis,
            duration.inWholeMilliseconds
        )

        return VerdandiInterval.normalized(
            VerdandiMoment(newStart, timeZoneId),
            VerdandiMoment(newEnd, timeZoneId)
        )
    }

    override fun contains(other: VerdandiMoment): Boolean {
        val momentMillis = other.epoch

        return momentMillis in startMillis..<endMillis
    }

    override fun overlaps(other: VerdandiInterval): Boolean {
        return startMillis < other.end.epoch && other.start.epoch < endMillis
    }

    override fun intersection(other: VerdandiInterval): VerdandiInterval? {
        val newStart = maxOf(startMillis, other.start.epoch)
        val newEnd = minOf(endMillis, other.end.epoch)

        if (newStart >= newEnd) {
            return null
        }

        return VerdandiInterval.normalized(
            VerdandiMoment(newStart, timeZoneId),
            VerdandiMoment(newEnd, timeZoneId)
        )
    }

    override fun union(other: VerdandiInterval): VerdandiInterval? {
        val touchesOrOverlaps = startMillis <= other.end.epoch &&
                other.start.epoch <= endMillis

        if (touchesOrOverlaps.not()) {
            return null
        }

        val newStart = minOf(startMillis, other.start.epoch)
        val newEnd = maxOf(endMillis, other.end.epoch)

        return VerdandiInterval.normalized(
            VerdandiMoment(newStart, timeZoneId),
            VerdandiMoment(newEnd, timeZoneId)
        )
    }

    override fun duration(): DateTimeDuration {
        return MomentDurationResolver.resolve(start, end)
    }

    override fun isEmpty(): Boolean {
        return startMillis >= endMillis
    }
}
