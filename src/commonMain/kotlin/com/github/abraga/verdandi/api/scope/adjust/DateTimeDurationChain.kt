package com.github.abraga.verdandi.api.scope.adjust

import com.github.abraga.verdandi.api.model.duration.DateDuration
import kotlin.time.Duration

class DateTimeDurationChain internal constructor(
    private val applyDateDuration: DateDuration.(Boolean) -> Unit,
    private val applyDuration: Duration.(Boolean) -> Unit,
) {

    operator fun plus(other: DateDuration): DateTimeDurationChain {
        applyDateDuration(other, true)

        return this
    }

    operator fun minus(other: DateDuration): DateTimeDurationChain {
        applyDateDuration(other, false)

        return this
    }

    operator fun plus(other: Duration): DateTimeDurationChain {
        applyDuration(other, true)

        return this
    }

    operator fun minus(other: Duration): DateTimeDurationChain {
        applyDuration(other, false)

        return this
    }
}
