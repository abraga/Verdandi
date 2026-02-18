package com.github.abraga.verdandi.internal.factory.parse

import com.github.abraga.verdandi.internal.core.VerdandiInstant
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.extension.verdandiParseError

internal class TimestampResolver {

    fun resolve(timestamp: String): Long {
        val text = timestamp.trim()

        resolveIsoWithZoneOrNull(text)?.let { return it }
        resolveIsoLocalOrNull(text)?.let { return it }
        resolveRfc9557OrNull(text)?.let { return it }
        resolveCompactOrNull(text)?.let { return it }
        resolveClfOrNull(text)?.let { return it }
        resolveIisOrNull(text)?.let { return it }
        resolveDdDotOrNull(text)?.let { return it }

        verdandiParseError("Unsupported timestamp format: '$timestamp'")
    }

    private fun resolveRfc9557OrNull(input: String): Long? {
        val match = RFC_9557.matchEntire(input) ?: return null
        val isoLocal = match.buildIsoLocal()
        val isoWithZone = isoLocal + normalizeZone(match.require(GROUP_ZONE))

        return VerdandiInstant.parse(isoWithZone).toUtcEpochMillis()
    }

    private fun resolveIsoWithZoneOrNull(input: String): Long? {
        val match = ISO_WITH_ZONE.matchEntire(input) ?: return null
        val isoLocal = match.buildIsoLocal()
        val isoWithZone = isoLocal + normalizeZone(match.require(GROUP_ZONE))

        return VerdandiInstant.parse(isoWithZone).toUtcEpochMillis()
    }

    private fun resolveIsoLocalOrNull(input: String): Long? {
        val match = ISO.matchEntire(input) ?: return null
        val isoLocal = match.buildIsoLocal()

        return parseLocalAsUtc(isoLocal)
    }

    private fun resolveDdDotOrNull(input: String): Long? {
        val match = DD_DOT.matchEntire(input) ?: return null
        val isoLocal = match.buildIsoLocal()

        return parseLocalAsUtc(isoLocal)
    }

    private fun resolveCompactOrNull(input: String): Long? {
        val match = COMPACT.matchEntire(input) ?: return null
        val isoLocal = match.buildIsoLocal()
        val zone = match.optionalOrNull(GROUP_ZONE) ?: return parseLocalAsUtc(isoLocal)

        return VerdandiInstant.parse(isoLocal + normalizeZone(zone)).toUtcEpochMillis()
    }

    private fun resolveClfOrNull(input: String): Long? {
        val normalized = input.removeSurrounding("[", "]").trim()
        val match = CLF_APACHE.matchEntire(normalized) ?: return null
        val month = monthNumber(match.require(GROUP_MONTH)) ?: return null

        val isoLocal = match.buildIsoLocal(month = month, fraction = "")
        val isoWithZone = isoLocal + normalizeZone(match.require(GROUP_ZONE))

        return VerdandiInstant.parse(isoWithZone).toUtcEpochMillis()
    }

    private fun resolveIisOrNull(input: String): Long? {
        val match = IIS_W3C.matchEntire(input) ?: return null
        val isoLocal = match.buildIsoLocal()

        return parseLocalAsUtc(isoLocal)
    }

    private fun MatchResult.buildIsoLocal(
        year: String? = null,
        month: String? = null,
        day: String? = null,
        hour: String? = null,
        minute: String? = null,
        second: String? = null,
        fraction: String? = null
    ): String {
        val yearValue = year ?: require(GROUP_YEAR)
        val monthValue = month ?: require(GROUP_MONTH)
        val dayValue = day ?: require(GROUP_DAY)
        val hourValue = hour ?: require(GROUP_HOUR)
        val minuteValue = minute ?: require(GROUP_MINUTE)
        val secondValue = second ?: require(GROUP_SECOND)
        val fractionValue = fraction ?: optionalOrNull(GROUP_FRACTION).orEmpty()
        val fractionPart = buildFractionOrEmpty(fractionValue)

        return "$yearValue-$monthValue-$dayValue$TIMESTAMP_T$hourValue:$minuteValue:$secondValue$fractionPart"
    }

    private fun buildFractionOrEmpty(fraction: String): String {
        if (fraction.isEmpty()) {
            return ""
        }

        val normalized = if (fraction.length >= 9) {
            fraction.take(9)
        } else {
            fraction.padEnd(9, '0')
        }

        return ".$normalized"
    }

    private fun normalizeZone(zone: String): String {
        if (zone == ZONE_UTC) {
            return ZONE_UTC
        }

        if (zone.length == 5 && (zone.startsWith(ZONE_PLUS) || zone.startsWith(ZONE_MINUS))) {
            return zone.take(3) + ZONE_COLON + zone.substring(3)
        }

        return zone
    }

    private fun monthNumber(value: String): String? {
        return MONTH_MAP[value.lowercase()]
    }

    private fun MatchResult.require(name: String): String {
        return groups[name]?.value?.takeIf { it.isNotEmpty() }
            ?: verdandiParseError("Missing component: $name")
    }

    private fun MatchResult.optionalOrNull(name: String): String? {
        val group = groups[name] ?: return null
        val value = group.value

        return value.ifEmpty { null }
    }

    private fun parseLocalAsUtc(isoLocal: String): Long {
        return VerdandiLocalDateTime.parse(isoLocal).toEpochMillisecondsUtc()
    }

    companion object {

        private const val GROUP_YEAR = "year"
        private const val GROUP_MONTH = "month"
        private const val GROUP_DAY = "day"
        private const val GROUP_HOUR = "hour"
        private const val GROUP_MINUTE = "minute"
        private const val GROUP_SECOND = "second"
        private const val GROUP_FRACTION = "fraction"
        private const val GROUP_ZONE = "zone"

        private const val TIMESTAMP_T = "T"

        private const val ZONE_UTC = "Z"
        private const val ZONE_PLUS = "+"
        private const val ZONE_MINUS = "-"
        private const val ZONE_COLON = ":"

        private val MONTH_MAP = mapOf(
            "jan" to "01", "feb" to "02", "mar" to "03", "apr" to "04",
            "may" to "05", "jun" to "06", "jul" to "07", "aug" to "08",
            "sep" to "09", "oct" to "10", "nov" to "11", "dec" to "12"
        )

        private val ISO = Regex(
            """^(?<year>\d{4})-(?<month>\d{2})-(?<day>\d{2})[T ](?<hour>\d{2}):(?<minute>\d{2}):(?<second>\d{2})(?:\.(?<fraction>\d{1,9}))?$"""
        )

        private val ISO_WITH_ZONE = Regex(
            """^(?<year>\d{4})-(?<month>\d{2})-(?<day>\d{2})[T ](?<hour>\d{2}):(?<minute>\d{2}):(?<second>\d{2})(?:\.(?<fraction>\d{1,9}))?(?<zone>Z|[+-]\d{2}:\d{2})$"""
        )

        private val DD_DOT = Regex(
            """^(?<day>\d{2})\.(?<month>\d{2})\.(?<year>\d{4})[ T](?<hour>\d{2}):(?<minute>\d{2}):(?<second>\d{2})(?:[.,](?<fraction>\d{1,9}))?$"""
        )

        private val COMPACT = Regex(
            """^(?<year>\d{4})(?<month>\d{2})(?<day>\d{2})T(?<hour>\d{2})(?<minute>\d{2})(?<second>\d{2})(?:\.(?<fraction>\d{1,9}))?(?<zone>Z|[+-]\d{2}:?\d{2})?$"""
        )

        private val RFC_9557 = Regex(
            """^(?<year>\d{4})-(?<month>\d{2})-(?<day>\d{2})T(?<hour>\d{2}):(?<minute>\d{2}):(?<second>\d{2})(?:\.(?<fraction>\d{1,9}))?(?<zone>Z|[+-]\d{2}:\d{2})\[(?<tzid>[A-Za-z_]+(?:/[A-Za-z0-9._+-]+)*)]$"""
        )

        private val CLF_APACHE = Regex(
            """^(?<day>\d{2})/(?<month>[A-Za-z]{3})/(?<year>\d{4}):(?<hour>\d{2}):(?<minute>\d{2}):(?<second>\d{2}) (?<zone>[+-]\d{4})$"""
        )

        private val IIS_W3C = Regex(
            """^(?<year>\d{4})-(?<month>\d{2})-(?<day>\d{2}) (?<hour>\d{2}):(?<minute>\d{2}):(?<second>\d{2})(?:\.(?<fraction>\d{1,9}))?$"""
        )
    }
}

