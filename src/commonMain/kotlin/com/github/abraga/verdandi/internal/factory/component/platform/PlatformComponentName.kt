package com.github.abraga.verdandi.internal.factory.component.platform

import com.github.abraga.verdandi.api.model.component.DayOfWeek
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale

internal expect fun Month.getMonthName(locale: VerdandiLocale): String

internal expect fun DayOfWeek.getDayOfWeekName(locale: VerdandiLocale): String

internal expect fun getAmPmName(locale: VerdandiLocale): Pair<String, String>
