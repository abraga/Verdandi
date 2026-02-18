package com.github.abraga.verdandi.api.scope.recurrence

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.recurrence.VerdandiRecurrenceMoments

interface RecurrenceRuleInProgress {

    infix fun until(moment: VerdandiMoment): VerdandiRecurrenceMoments
}
