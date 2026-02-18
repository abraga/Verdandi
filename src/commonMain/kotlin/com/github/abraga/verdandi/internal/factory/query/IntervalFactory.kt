package com.github.abraga.verdandi.internal.factory.query

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.scope.factory.VerdandiIntervalFactoryScope
import com.github.abraga.verdandi.api.scope.QuantityUnitSelector
import com.github.abraga.verdandi.internal.factory.query.model.QueryData
import com.github.abraga.verdandi.internal.factory.query.temporal.TemporalDirection

internal class IntervalFactory : VerdandiIntervalFactoryScope {

    private val query: QueryData = QueryData()

    override val last: QuantityUnitSelector<VerdandiInterval>
        get() = QuantityIntervalBuilder(query.copy(temporalDirection = TemporalDirection.LAST))

    override val next: QuantityUnitSelector<VerdandiInterval>
        get() = QuantityIntervalBuilder(query.copy(temporalDirection = TemporalDirection.NEXT))
}
