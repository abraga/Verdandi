package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.recurrence.VerdandiRecurrenceScope
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceEndMarker
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceIntervalSelector
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceOnMarker
import com.github.abraga.verdandi.api.scope.recurrence.RecurrenceTimeMarker
import kotlin.time.Duration

internal class RecurrenceScope(
    private val startMoment: VerdandiMoment,
    private val limit: Int?
) : VerdandiRecurrenceScope {

    override val every: RecurrenceIntervalSelector
        get() = RecurrenceIntervalSelector(startMoment, limit = limit)

    override val on: RecurrenceOnMarker
        get() = RecurrenceOnMarker

    override val indefinitely: RecurrenceEndMarker
        get() = RecurrenceEndMarker(moment = null)

    override fun at(block: () -> Duration): RecurrenceTimeMarker {
        return RecurrenceTimeMarker(block())
    }

    override fun at(time: Duration): RecurrenceTimeMarker {
        return RecurrenceTimeMarker(time)
    }

    override infix fun until(moment: VerdandiMoment): RecurrenceEndMarker {
        return RecurrenceEndMarker(moment)
    }
}
