package com.github.abraga.verdandi.internal.factory.query

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.factory.VerdandiFromMomentFactoryScope
import com.github.abraga.verdandi.internal.extension.verdandiStateError
import com.github.abraga.verdandi.internal.factory.query.model.QueryData
import com.github.abraga.verdandi.internal.factory.query.temporal.TemporalDirection

internal class FromMomentFactory(private val query: QueryData?) : VerdandiFromMomentFactoryScope {

    override fun from(moment: VerdandiMoment): VerdandiInterval {
        val queryData = query ?: verdandiStateError("Missing query data.")
        val durationMillis = queryData.timeUnit.resolveMilliseconds(queryData.timeCount, moment.epoch)

        if (queryData.temporalDirection == TemporalDirection.LAST) {
            val startMillis = moment.epoch - durationMillis
            val start = VerdandiMoment(startMillis)

            return VerdandiInterval.normalized(start, moment)
        }

        val endMillis = moment.epoch + durationMillis
        val end = VerdandiMoment(endMillis)

        return VerdandiInterval.normalized(moment, end)
    }
}
