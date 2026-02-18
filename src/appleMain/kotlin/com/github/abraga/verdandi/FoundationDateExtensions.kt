package com.github.abraga.verdandi

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import platform.Foundation.NSDate
import platform.Foundation.NSTimeZone
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.timeZoneWithName

private const val MILLIS_PER_SECOND = 1000.0

fun VerdandiMoment.toNSDate(): NSDate {
    return NSDate.dateWithTimeIntervalSince1970(inMilliseconds.toDouble() / MILLIS_PER_SECOND)
}

fun NSDate.toVerdandiMoment(): VerdandiMoment {
    return Verdandi.from((timeIntervalSince1970 * MILLIS_PER_SECOND).toLong())
}

fun VerdandiTimeZone.toNSTimeZone(): NSTimeZone {
    return NSTimeZone.timeZoneWithName(id)!!
}

fun NSTimeZone.toVerdandiTimeZone(): VerdandiTimeZone {
    return VerdandiTimeZone.of(name)
}
