package com.github.abraga.verdandi.internal.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import kotlin.time.Duration

internal class RecurrenceRule(
    val frequency: RecurrenceFrequency,
    val interval: Int,
    val daysOfWeek: Set<RecurrenceDayOfWeek>,
    val timeOffset: Duration?,
    val endBoundary: VerdandiMoment?
)
