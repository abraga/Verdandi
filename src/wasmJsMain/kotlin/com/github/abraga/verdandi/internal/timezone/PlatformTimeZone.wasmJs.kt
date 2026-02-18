@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.timezone

internal actual object PlatformTimeZone {

    actual fun offsetMillisAt(timeZoneId: String, epochMillis: Long): Long {
        return jsOffsetMillisAt(timeZoneId, epochMillis.toDouble()).toLong()
    }

    actual fun isTimeZoneIdValid(timeZoneId: String): Boolean {
        return jsIsTimeZoneIdValid(timeZoneId)
    }

    actual fun defaultId(): String {
        return jsDefaultId()
    }

    actual fun availableIds(): Set<String> {
        val array = jsAvailableIds()
        val size = array.length

        return buildSet {
            for (i in 0 until size) {
                add(array[i]!!.toString())
            }
        }
    }
}

@JsFun(
    """
    (timeZoneId, epochMillis) => {
        try {
            const date = new Date(epochMillis);
            const parts = new Intl.DateTimeFormat('en-US', {
                timeZone: timeZoneId,
                year: 'numeric',
                month: 'numeric',
                day: 'numeric',
                hour: 'numeric',
                minute: 'numeric',
                second: 'numeric',
                hour12: false
            }).formatToParts(date);

            const get = (type) => parseInt(parts.find(p => p.type === type).value);

            let y = get('year');
            let m = get('month') - 1;
            let d = get('day');
            let h = get('hour');
            if (h === 24) h = 0;
            let min = get('minute');
            let s = get('second');

            const ms = epochMillis % 1000;
            const localInTz = Date.UTC(y, m, d, h, min, s) + ms;

            return localInTz - epochMillis;
        } catch (e) {
            return 0;
        }
    }
    """
)
private external fun jsOffsetMillisAt(timeZoneId: String, epochMillis: Double): Double

@JsFun(
    """
    (timeZoneId) => {
        try {
            Intl.DateTimeFormat(undefined, { timeZone: timeZoneId });
            return true;
        } catch (e) {
            return false;
        }
    }
    """
)
private external fun jsIsTimeZoneIdValid(timeZoneId: String): Boolean

@JsFun("() => Intl.DateTimeFormat().resolvedOptions().timeZone")
private external fun jsDefaultId(): String

@JsFun(
    """
    () => {
        try {
            return Intl.supportedValuesOf('timeZone');
        } catch (e) {
            return [];
        }
    }
    """
)
private external fun jsAvailableIds(): JsArray<JsString>
