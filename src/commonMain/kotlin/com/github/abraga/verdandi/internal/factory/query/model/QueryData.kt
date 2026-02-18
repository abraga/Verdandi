package com.github.abraga.verdandi.internal.factory.query.model

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.internal.factory.query.temporal.TemporalDirection

internal data class QueryData(
    val temporalDirection: TemporalDirection = TemporalDirection.LAST,
    val timeCount: Int = -1,
    val timeUnit: TemporalUnit<*> = TemporalUnit.Millisecond,
    val temporalMoment: VerdandiMoment? = null
)
