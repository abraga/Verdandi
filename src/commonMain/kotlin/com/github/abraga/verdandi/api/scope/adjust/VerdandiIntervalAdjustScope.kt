package com.github.abraga.verdandi.api.scope.adjust

import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import kotlin.time.Duration

@VerdandiDslMarker
interface VerdandiIntervalAdjustScope {

    fun shiftBoth(duration: Duration)

    fun shiftStart(duration: Duration)

    fun shiftEnd(duration: Duration)

    fun expandBoth(duration: Duration)

    fun shrinkBoth(duration: Duration)

    fun alignToFullDays()

    fun alignToFullMonths()

    fun alignToFullYears()

    fun setDurationFromStart(duration: Duration)

    fun setDurationFromEnd(duration: Duration)

    operator fun plusAssign(duration: Duration)

    operator fun minusAssign(duration: Duration)
}
