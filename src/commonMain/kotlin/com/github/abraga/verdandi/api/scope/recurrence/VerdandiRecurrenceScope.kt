package com.github.abraga.verdandi.api.scope.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import com.github.abraga.verdandi.internal.recurrence.RecurrenceScope
import kotlin.time.Duration

@VerdandiDslMarker
interface VerdandiRecurrenceScope {

    val every: RecurrenceIntervalSelector

    val on: RecurrenceOnMarker

    val indefinitely: RecurrenceEndMarker

    fun at(block: () -> Duration): RecurrenceTimeMarker

    fun at(time: Duration): RecurrenceTimeMarker

    infix fun until(moment: VerdandiMoment): RecurrenceEndMarker

    companion object {

        internal operator fun invoke(
            startMoment: VerdandiMoment,
            limit: Int?
        ): VerdandiRecurrenceScope {
            return RecurrenceScope(startMoment, limit)
        }
    }
}
