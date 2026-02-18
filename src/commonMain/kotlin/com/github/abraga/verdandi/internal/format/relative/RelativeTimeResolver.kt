package com.github.abraga.verdandi.internal.format.relative

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.relative.Future
import com.github.abraga.verdandi.internal.duration.MomentDurationResolver
import com.github.abraga.verdandi.api.model.relative.VerdandiRelativeComponents
import com.github.abraga.verdandi.api.model.relative.VerdandiRelativeMoment
import com.github.abraga.verdandi.api.model.relative.Now
import com.github.abraga.verdandi.api.model.relative.Past
import kotlin.math.abs
import kotlin.time.Duration

internal class RelativeTimeResolver(private val nowThreshold: Duration) {

    fun resolve(moment: VerdandiMoment, referencePoint: VerdandiMoment): VerdandiRelativeMoment {
        val diffMs = moment.epoch - referencePoint.epoch
        val absDiffMs = abs(diffMs)

        if (absDiffMs < nowThreshold.inWholeMilliseconds) {
            return Now
        }

        val duration = MomentDurationResolver.resolve(moment, referencePoint)

        val components = VerdandiRelativeComponents(
            years = duration.years,
            months = duration.months,
            weeks = duration.weeks,
            days = duration.days,
            hours = duration.hours,
            minutes = duration.minutes,
            seconds = duration.seconds
        )

        if (diffMs < 0) {
            return Past(components)
        }

        return Future(components)
    }
}
