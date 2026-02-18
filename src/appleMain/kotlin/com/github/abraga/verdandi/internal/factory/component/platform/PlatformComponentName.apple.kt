package com.github.abraga.verdandi.internal.factory.component.platform

import com.github.abraga.verdandi.api.model.component.DayOfWeek
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale
import platform.Foundation.NSCalendar
import platform.Foundation.NSLocale

internal actual fun Month.getMonthName(locale: VerdandiLocale): String {
    val calendar = getCalendar(locale)
    val monthNames = calendar.monthSymbols

    return monthNames[value - 1] as String
}

internal actual fun DayOfWeek.getDayOfWeekName(locale: VerdandiLocale): String {
    val calendar = getCalendar(locale)
    val weekdayNames = calendar.weekdaySymbols
    val index = if (value == 7) 0 else value

    return weekdayNames[index] as String
}

internal actual fun getAmPmName(locale: VerdandiLocale): Pair<String, String> {
    val calendar = getCalendar(locale)

    return calendar.AMSymbol to calendar.PMSymbol
}

private fun getCalendar(locale: VerdandiLocale): NSCalendar {
    val nsLocale = NSLocale(localeIdentifier = locale.identifier)

    return NSCalendar.currentCalendar.apply {
        this.locale = nsLocale
    }
}
