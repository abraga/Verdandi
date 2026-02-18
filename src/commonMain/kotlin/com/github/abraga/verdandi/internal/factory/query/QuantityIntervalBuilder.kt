package com.github.abraga.verdandi.internal.factory.query

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.api.scope.QuantityUnitSelector
import com.github.abraga.verdandi.internal.extension.currentTimeMillis
import com.github.abraga.verdandi.internal.factory.query.model.QueryData
import com.github.abraga.verdandi.internal.factory.query.temporal.TemporalDirection

internal class QuantityIntervalBuilder(private val query: QueryData) : QuantityUnitSelector<VerdandiInterval> {

    override fun build(count: Int, unit: TemporalUnit<*>): VerdandiInterval {
        val anchorMillis = query.temporalMoment?.epoch ?: currentTimeMillis()
        val durationMillis = unit.resolveMilliseconds(count, anchorMillis)
        val resolvedQuery = query.copy(timeCount = count, timeUnit = unit)

        return if (query.temporalDirection == TemporalDirection.LAST) {
            val start = VerdandiMoment(anchorMillis - durationMillis)
            val end = VerdandiMoment(anchorMillis)
            VerdandiInterval.normalized(start, end, resolvedQuery)
        } else {
            val start = VerdandiMoment(anchorMillis)
            val end = VerdandiMoment(anchorMillis + durationMillis)
            VerdandiInterval.normalized(start, end, resolvedQuery)
        }
    }
}
