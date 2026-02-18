package com.github.abraga.verdandi.internal.factory.query

import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.internal.DateTimeConstants.FIRST_DAY
import com.github.abraga.verdandi.internal.DateTimeConstants.FIRST_MONTH
import com.github.abraga.verdandi.internal.DateTimeConstants.LAST_DAY_OF_WEEK
import com.github.abraga.verdandi.internal.DateTimeConstants.LAST_MONTH
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_SECOND
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.extension.verdandiConfigurationError

internal fun TemporalUnit<*>.resolveMilliseconds(value: Int, anchorMillis: Long): Long {
    return when (id) {
        0 -> value * 1L
        1 -> value * MILLIS_PER_SECOND
        2 -> value * MILLIS_PER_MINUTE
        3 -> value * MILLIS_PER_HOUR
        4 -> resolveDateBased(value, anchorMillis, days = FIRST_DAY)
        5 -> resolveDateBased(value, anchorMillis, days = LAST_DAY_OF_WEEK)
        6 -> resolveDateBased(value, anchorMillis, months = FIRST_MONTH)
        7 -> resolveDateBased(value, anchorMillis, months = LAST_MONTH)
        else -> verdandiConfigurationError("Unknown factory unit id: $id")
    }
}

private fun resolveDateBased(
    value: Int,
    anchorMillis: Long,
    days: Int = 0,
    months: Int = 0
): Long {
    val baseDateTime = VerdandiLocalDateTime.fromEpochMillisecondsUtc(anchorMillis)

    val endDateTime = when {
        months > 0 -> baseDateTime.plusMonths(value.toLong() * months)
        days > 0 -> baseDateTime.plusDays(value.toLong() * days)
        else -> baseDateTime
    }

    return endDateTime.toEpochMillisecondsUtc() - anchorMillis
}
