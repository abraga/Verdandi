@file:Suppress("DEPRECATION")

package com.github.abraga.verdandi.internal.factory.component.platform

import com.github.abraga.verdandi.api.model.component.DayOfWeek
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale
import java.text.DateFormatSymbols
import java.util.Locale

internal actual fun Month.getMonthName(locale: VerdandiLocale): String {
    val symbols = getSymbols(locale)

    return symbols.months[value - 1]
}

internal actual fun DayOfWeek.getDayOfWeekName(locale: VerdandiLocale): String {
    val symbols = getSymbols(locale)
    val index = if (value == 7) 1 else value + 1

    return symbols.weekdays[index]
}

internal actual fun getAmPmName(locale: VerdandiLocale): Pair<String, String> {
    val symbols = getSymbols(locale)

    return symbols.amPmStrings[0] to symbols.amPmStrings[1]
}

private fun getSymbols(locale: VerdandiLocale): DateFormatSymbols {
    val javaLocale = Locale(locale.language, locale.country)

    return DateFormatSymbols.getInstance(javaLocale)
}
