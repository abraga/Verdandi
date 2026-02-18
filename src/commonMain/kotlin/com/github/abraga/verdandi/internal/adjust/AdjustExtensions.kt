package com.github.abraga.verdandi.internal.adjust

import com.github.abraga.verdandi.internal.core.VerdandiLocalDate

internal val VerdandiLocalDate.firstDayOfMonth: VerdandiLocalDate
    get() = VerdandiLocalDate.of(year, monthNumber, 1)

internal val VerdandiLocalDate.lastDayOfMonth: VerdandiLocalDate
    get() = firstDayOfMonth.plusMonths(1).minusDays(1)

internal val VerdandiLocalDate.firstDayOfYear: VerdandiLocalDate
    get() = VerdandiLocalDate.of(year, 1, 1)

internal val VerdandiLocalDate.lastDayOfYear: VerdandiLocalDate
    get() = VerdandiLocalDate.of(year, 12, 31)
