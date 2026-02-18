@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.zone

internal actual class CurrentTimeZone actual constructor() {

    actual val id: String
        get() = jsGetTimeZoneId()

    actual val offsetMillis: Long
        get() = jsGetTimezoneOffsetMinutes().toLong() * -60_000L

    actual val displayName: String
        get() = jsGetDisplayName()

    actual val abbreviation: String
        get() = jsGetAbbreviation()

    actual val isDaylightSavingTime: Boolean
        get() = jsIsDaylightSavingTime()

    actual val daylightSavingTimeOffsetMillis: Long
        get() {
            if (!isDaylightSavingTime) return 0L

            return jsGetDstOffsetMillis().toLong()
        }

    actual val totalOffsetMillis: Long
        get() = offsetMillis + daylightSavingTimeOffsetMillis
}

@JsFun("() => Intl.DateTimeFormat().resolvedOptions().timeZone")
private external fun jsGetTimeZoneId(): String

@JsFun("() => new Date().getTimezoneOffset()")
private external fun jsGetTimezoneOffsetMinutes(): Int

@JsFun(
    """
    () => {
        var timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        var parts = new Intl.DateTimeFormat(navigator.language, { timeZone: timeZone, timeZoneName: 'long' }).formatToParts(new Date());
        var tzPart = parts.find(function(part) { return part.type === 'timeZoneName'; });
        return tzPart ? tzPart.value : timeZone;
    }
    """
)
private external fun jsGetDisplayName(): String

@JsFun(
    """
    () => {
        var timeZone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        var parts = new Intl.DateTimeFormat(navigator.language, { timeZone: timeZone, timeZoneName: 'short' }).formatToParts(new Date());
        var tzPart = parts.find(function(part) { return part.type === 'timeZoneName'; });
        return tzPart ? tzPart.value : '';
    }
    """
)
private external fun jsGetAbbreviation(): String

@JsFun(
    """
    () => {
        var now = new Date();
        var jan = new Date(now.getFullYear(), 0, 1);
        var jul = new Date(now.getFullYear(), 6, 1);
        var stdOffset = Math.max(jan.getTimezoneOffset(), jul.getTimezoneOffset());
        return now.getTimezoneOffset() < stdOffset;
    }
    """
)
private external fun jsIsDaylightSavingTime(): Boolean

@JsFun(
    """
    () => {
        var now = new Date();
        var jan = new Date(now.getFullYear(), 0, 1);
        var jul = new Date(now.getFullYear(), 6, 1);
        var stdOffset = Math.max(jan.getTimezoneOffset(), jul.getTimezoneOffset());
        return (stdOffset - now.getTimezoneOffset()) * 60000;
    }
    """
)
private external fun jsGetDstOffsetMillis(): Int
