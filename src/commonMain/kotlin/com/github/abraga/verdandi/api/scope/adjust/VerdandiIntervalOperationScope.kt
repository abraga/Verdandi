package com.github.abraga.verdandi.api.scope.adjust

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.duration.DateTimeDuration
import kotlin.time.Duration

interface VerdandiIntervalOperationScope {

    operator fun plus(duration: Duration): VerdandiInterval

    operator fun minus(duration: Duration): VerdandiInterval

    fun expand(duration: Duration): VerdandiInterval

    fun shrink(duration: Duration): VerdandiInterval

    fun contains(other: VerdandiMoment): Boolean

    fun overlaps(other: VerdandiInterval): Boolean

    fun intersection(other: VerdandiInterval): VerdandiInterval?

    fun union(other: VerdandiInterval): VerdandiInterval?

    fun duration(): DateTimeDuration

    fun isEmpty(): Boolean
}
