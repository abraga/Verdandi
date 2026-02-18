package com.github.abraga.verdandi.internal.factory.component.platform

import com.github.abraga.verdandi.api.model.component.DayOfWeek
import com.github.abraga.verdandi.api.model.component.Month
import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale

internal actual fun Month.getMonthName(locale: VerdandiLocale): String {
    return jsGetMonthName(value - 1, locale.identifier)
}

internal actual fun DayOfWeek.getDayOfWeekName(locale: VerdandiLocale): String {
    return jsGetDayOfWeekName(value, locale.identifier)
}

internal actual fun getAmPmName(locale: VerdandiLocale): Pair<String, String> {
    return jsGetAmName(locale.identifier) to jsGetPmName(locale.identifier)
}

@JsFun(
    """
    (monthIndex, locale) => {
        const date = new Date(2000, monthIndex, 1);
        return date.toLocaleString(locale.replace('_', '-'), { month: 'long' });
    }
    """
)
private external fun jsGetMonthName(monthIndex: Int, locale: String): String

@JsFun(
    """
    (dayOfWeek, locale) => {
        const mondayOffset = 2;
        const date = new Date(2000, 0, mondayOffset + dayOfWeek);
        return date.toLocaleString(locale.replace('_', '-'), { weekday: 'long' });
    }
    """
)
private external fun jsGetDayOfWeekName(dayOfWeek: Int, locale: String): String

@JsFun(
    """
    (locale) => {
        const tag = locale.replace('_', '-');
        const parts = new Intl.DateTimeFormat(tag, { hour: 'numeric', hour12: true })
            .formatToParts(new Date(2000, 0, 1, 6, 0));
        const period = parts.find(p => p.type === 'dayPeriod');
        return period ? period.value : 'AM';
    }
    """
)
private external fun jsGetAmName(locale: String): String

@JsFun(
    """
    (locale) => {
        const tag = locale.replace('_', '-');
        const parts = new Intl.DateTimeFormat(tag, { hour: 'numeric', hour12: true })
            .formatToParts(new Date(2000, 0, 1, 18, 0));
        const period = parts.find(p => p.type === 'dayPeriod');
        return period ? period.value : 'PM';
    }
    """
)
private external fun jsGetPmName(locale: String): String
