@file:OptIn(ExperimentalAtomicApi::class)

package com.github.abraga.verdandi.internal.format.string

import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.internal.DateTimeConstants.MAX_NANOSECOND
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_HOUR
import com.github.abraga.verdandi.internal.DateTimeConstants.MILLIS_PER_MINUTE
import com.github.abraga.verdandi.internal.DateTimeConstants.NANOS_PER_MILLI
import com.github.abraga.verdandi.internal.core.VerdandiLocalDateTime
import com.github.abraga.verdandi.internal.extension.verdandiParseError
import com.github.abraga.verdandi.internal.extension.verdandiRequireParse
import com.github.abraga.verdandi.internal.extension.timeZoneContext
import com.github.abraga.verdandi.internal.timezone.TimeZoneContext
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.update
import kotlin.jvm.JvmField

internal class StringPatternParser private constructor() {

    companion object {

        private const val MONTH_ABBREV_HASH_SIZE: Int = 26 * 26 * 26

        private val MONTH_BY_ABBREVIATION_HASH: Array<Int?> =
            arrayOfNulls<Int>(MONTH_ABBREV_HASH_SIZE).also { monthByHash ->

                fun abbreviationHash(first: Char, second: Char, third: Char): Int {
                    return (first - 'a') * 676 + (second - 'a') * 26 + (third - 'a')
                }

                monthByHash[abbreviationHash('j', 'a', 'n')] = 1
                monthByHash[abbreviationHash('f', 'e', 'b')] = 2
                monthByHash[abbreviationHash('m', 'a', 'r')] = 3
                monthByHash[abbreviationHash('a', 'p', 'r')] = 4
                monthByHash[abbreviationHash('m', 'a', 'y')] = 5
                monthByHash[abbreviationHash('j', 'u', 'n')] = 6
                monthByHash[abbreviationHash('j', 'u', 'l')] = 7
                monthByHash[abbreviationHash('a', 'u', 'g')] = 8
                monthByHash[abbreviationHash('s', 'e', 'p')] = 9
                monthByHash[abbreviationHash('o', 'c', 't')] = 10
                monthByHash[abbreviationHash('n', 'o', 'v')] = 11
                monthByHash[abbreviationHash('d', 'e', 'c')] = 12
            }

        private val MONTH_FULL_NAME_ENTRIES: Array<Pair<CharArray, Int>> = arrayOf(
            "september".toCharArray() to 9,
            "february".toCharArray() to 2,
            "november".toCharArray() to 11,
            "december".toCharArray() to 12,
            "january".toCharArray() to 1,
            "october".toCharArray() to 10,
            "august".toCharArray() to 8,
            "march".toCharArray() to 3,
            "april".toCharArray() to 4,
            "july".toCharArray() to 7,
            "june".toCharArray() to 6,
            "may".toCharArray() to 5
        )

        private val tokensByPatternCache =
            AtomicReference<Map<String, Array<FormatPatternToken>>>(emptyMap())

        operator fun invoke(
            input: String,
            pattern: String,
            context: TimeZoneContext? = null
        ): VerdandiMoment {
            return parse(input, pattern, context)
        }

        private fun parse(
            input: String,
            pattern: String,
            explicitContext: TimeZoneContext?
        ): VerdandiMoment {
            var inputIndex = 0
            val parsedComponents = ParsedComponents(explicitContext)
            val tokens = resolveTokens(pattern)

            var tokenIndex = 0

            while (tokenIndex < tokens.size) {
                val token = tokens[tokenIndex]

                inputIndex = when (token) {
                    is FormatPatternToken.LiteralToken -> {
                        matchLiteral(input, inputIndex, token.text)
                    }

                    is FormatPatternToken.DirectiveToken -> {
                        parseDirective(
                            input = input,
                            position = inputIndex,
                            directive = token.pattern,
                            boundary = nextLiteralBoundary(tokens, tokenIndex),
                            parsed = parsedComponents
                        )
                    }
                }

                tokenIndex++
            }

            verdandiRequireParse(inputIndex == input.length) {
                "Unexpected trailing characters starting at index" +
                        " $inputIndex: '${input.substring(inputIndex)}'. " +
                        "Input='$input', Pattern='$pattern'."
            }

            return parsedComponents.toMoment()
        }

        private fun resolveTokens(pattern: String): Array<FormatPatternToken> {
            val currentCache = tokensByPatternCache.load()
            val existing = currentCache[pattern]

            if (existing != null) {
                return existing
            }

            val compiled = FormatPatternTokenizer(pattern).toTypedArray()

            tokensByPatternCache.update { it + (pattern to compiled) }

            return compiled
        }

        private fun matchLiteral(input: String, position: Int, expectedLiteral: String): Int {
            verdandiRequireParse(input.startsWith(expectedLiteral, position)) {
                val actualSliceEnd = (position + expectedLiteral.length).coerceAtMost(input.length)
                val actualSlice = input.substring(position, actualSliceEnd)
                "Literal mismatch at index $position: expected" +
                        " '$expectedLiteral', but found '$actualSlice'. " +
                        "Input='$input'."
            }

            return position + expectedLiteral.length
        }

        private fun parseDirective(
            input: String,
            position: Int,
            directive: String,
            boundary: Char?,
            parsed: ParsedComponents
        ): Int {
            val directiveLength = directive.length
            val directiveChar = directive[0]

            return when (directiveChar) {
                'y' if directiveLength == 4 -> parseFixedInt(input, position, 4) {
                    parsed.year = it
                }
                'y' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.year = 2000 + it
                }

                'M' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.month = it
                }
                'M' if directiveLength == 3 -> parseMonthAbbreviation(input, position, parsed)
                'M' if directiveLength == 4 -> parseMonthFullName(input, position, parsed)

                'd' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.day = it
                }
                'd' if directiveLength == 1 -> parseVariableInt(input, position) {
                    parsed.day = it
                }

                'H' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.hour = it
                }
                'h' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.hour = it
                    parsed.is12Hour = true
                }

                'm' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.minute = it
                }
                's' if directiveLength == 2 -> parseFixedInt(input, position, 2) {
                    parsed.second = it
                }
                'S' if directiveLength == 3 -> parseFixedInt(input, position, 3) {
                    parsed.millisecond = it
                }

                'Z' -> parseTimezoneOffset(input, position, parsed)
                'a' -> parseAmPmMarker(input, position, parsed)

                'E' if directiveLength == 3 -> position + 3
                'E' if directiveLength == 4 -> skipUntilBoundary(input, position, boundary)

                'Q' -> parseFixedInt(input, position, 1) { }

                else -> verdandiParseError(
                    "Unsupported parse directive '$directive' at index $position. Input='$input'."
                )
            }
        }

        private inline fun parseFixedInt(
            input: String,
            position: Int,
            length: Int,
            setter: (Int) -> Unit
        ): Int {
            val endExclusive = position + length

            verdandiRequireParse(endExclusive <= input.length) {
                "Unexpected end of input at index $position:" +
                        " expected $length digits, but only ${input.length - position} remaining." +
                        " Input='$input'."
            }

            var index = position
            var parsedValue = 0

            while (index < endExclusive) {
                val digit = input[index] - '0'

                if (digit !in 0..9) {
                    val slice = input.substring(position, endExclusive)

                    verdandiParseError(
                        "Invalid digit in numeric field at index" +
                                " $index: expected digit, but found '${input[index]}'." +
                                " Slice='$slice', Input='$input'."
                    )
                }

                parsedValue = parsedValue * 10 + digit
                index++
            }

            setter(parsedValue)

            return endExclusive
        }

        private inline fun parseVariableInt(
            input: String,
            position: Int,
            setter: (Int) -> Unit
        ): Int {
            val inputLength = input.length
            var endExclusive = position
            var parsedValue = 0

            while (endExclusive < inputLength) {
                val digit = input[endExclusive] - '0'
                if (digit !in 0..9) {
                    break
                }

                parsedValue = parsedValue * 10 + digit
                endExclusive++
            }

            verdandiRequireParse(endExclusive > position) {
                "Expected at least one digit at index $position," +
                        " but found '${input.getOrNull(position)}'. Input='$input'."
            }

            setter(parsedValue)

            return endExclusive
        }

        private fun parseTimezoneOffset(input: String, position: Int, parsed: ParsedComponents): Int {
            verdandiRequireParse(position < input.length) {
                "Unexpected end of input: expected timezone offset at index $position. Input='$input'."
            }

            if (input[position] == 'Z') {
                parsed.offsetMillis = 0L
                return position + 1
            }

            val endExclusive = position + 6

            verdandiRequireParse(endExclusive <= input.length) {
                "Unexpected end of input: expected timezone" +
                        " offset in format '+HH:MM' or '-HH:MM' at index $position." +
                        " Input='$input'."
            }

            val signMultiplier = when (input[position]) {
                '+' -> 1L
                '-' -> -1L
                else -> verdandiParseError(
                    "Invalid timezone offset sign at index $position:" +
                            " expected '+', '-' or 'Z', but found '${input[position]}'." +
                            " Input='$input'."
                )
            }

            val hourTens = input[position + 1] - '0'
            val hourOnes = input[position + 2] - '0'

            if (hourTens !in 0..9 || hourOnes !in 0..9) {
                val slice = input.substring(position, endExclusive)
                verdandiParseError(
                    "Invalid timezone offset hours at index" +
                            " ${position + 1}: expected 2 digits," +
                            " but found '${input.substring(position + 1, position + 3)}'." +
                            " OffsetSlice='$slice', Input='$input'."
                )
            }

            val minuteTens = input[position + 4] - '0'
            val minuteOnes = input[position + 5] - '0'

            if (minuteTens !in 0..9 || minuteOnes !in 0..9) {
                val slice = input.substring(position, endExclusive)
                verdandiParseError(
                    "Invalid timezone offset minutes at index ${position + 4}:" +
                            " expected 2 digits, but found" +
                            " '${input.substring(position + 4, position + 6)}'." +
                            " OffsetSlice='$slice', Input='$input'."
                )
            }

            val hours = hourTens * 10 + hourOnes
            val minutes = minuteTens * 10 + minuteOnes

            parsed.offsetMillis = signMultiplier * (hours * MILLIS_PER_HOUR + minutes * MILLIS_PER_MINUTE)

            return endExclusive
        }

        private fun parseAmPmMarker(input: String, position: Int, parsed: ParsedComponents): Int {
            verdandiRequireParse(position + 2 <= input.length) {
                "Unexpected end of input: expected AM/PM marker at index $position. Input='$input'."
            }

            val first = input[position]
            val second = input[position + 1]

            val isPm = first == 'p' || first == 'P'
            val isAm = first == 'a' || first == 'A'
            val hasM = second == 'm' || second == 'M'

            verdandiRequireParse((isPm || isAm) && hasM) {
                val actual = input.substring(position, position + 2)
                "Invalid AM/PM marker at index $position:" +
                        " expected 'AM' or 'PM', but found '$actual'. Input='$input'."
            }

            parsed.isPm = isPm

            return position + 2
        }

        private fun parseMonthAbbreviation(input: String, position: Int, parsed: ParsedComponents): Int {
            verdandiRequireParse(position + 3 <= input.length) {
                "Unexpected end of input: expected 3-letter" +
                        " month abbreviation at index $position. Input='$input'."
            }

            val first = input[position].lowercaseChar()
            val second = input[position + 1].lowercaseChar()
            val third = input[position + 2].lowercaseChar()

            if (first in 'a'..'z' && second in 'a'..'z' && third in 'a'..'z') {
                val hash = (first - 'a') * 676 + (second - 'a') * 26 + (third - 'a')
                val monthNumber = MONTH_BY_ABBREVIATION_HASH[hash]

                if (monthNumber != null) {
                    parsed.month = monthNumber
                    return position + 3
                }
            }

            val abbreviation = input.substring(position, position + 3)
            verdandiParseError(
                "Unknown month abbreviation '$abbreviation' at index $position. Input='$input'."
            )
        }

        private fun parseMonthFullName(input: String, position: Int, parsed: ParsedComponents): Int {
            val remainingChars = input.length - position

            for ((monthNameChars, monthNumber) in MONTH_FULL_NAME_ENTRIES) {
                val monthNameLength = monthNameChars.size

                if (monthNameLength > remainingChars) {
                    continue
                }

                var index = 0
                var matches = true

                while (index < monthNameLength) {
                    if (input[position + index].lowercaseChar() != monthNameChars[index]) {
                        matches = false
                        break
                    }

                    index++
                }

                if (matches) {
                    parsed.month = monthNumber
                    return position + monthNameLength
                }
            }

            verdandiParseError("Unknown month name at index $position. Input='$input'.")
        }

        private fun skipUntilBoundary(input: String, position: Int, boundary: Char?): Int {
            if (boundary == null) {
                return input.length
            }

            val boundaryIndex = input.indexOf(boundary, position)

            if (boundaryIndex == -1) {
                return input.length
            }

            return boundaryIndex
        }

        private fun nextLiteralBoundary(
            tokens: Array<FormatPatternToken>,
            currentIndex: Int
        ): Char? {
            val nextIndex = currentIndex + 1

            if (nextIndex >= tokens.size) {
                return null
            }

            val nextToken = tokens[nextIndex]

            if (nextToken is FormatPatternToken.LiteralToken && nextToken.text.isNotEmpty()) {
                return nextToken.text[0]
            }

            return null
        }
    }

    private class ParsedComponents(private val explicitContext: TimeZoneContext?) {

        @JvmField
        var year: Int = 0
        @JvmField
        var month: Int = 1
        @JvmField
        var day: Int = 1
        @JvmField
        var hour: Int = 0
        @JvmField
        var minute: Int = 0
        @JvmField
        var second: Int = 0
        @JvmField
        var millisecond: Int = 0
        @JvmField
        var offsetMillis: Long? = null
        @JvmField
        var isPm: Boolean = false
        @JvmField
        var is12Hour: Boolean = false

        fun toMoment(): VerdandiMoment {
            val resolvedHour = resolveHour()
            val localDateTime = buildLocalDateTime(resolvedHour)

            val offset = offsetMillis
            if (offset != null) {
                val utcMillis = localDateTime.toEpochMillisecondsUtc() - offset
                return VerdandiMoment(utcMillis)
            }

            val context = explicitContext ?: timeZoneContext
            val epochMillis = context.toEpochMillis(localDateTime)

            return VerdandiMoment(epochMillis, explicitContext?.timeZoneId)
        }

        private fun resolveHour(): Int {
            if (is12Hour.not()) {
                return hour
            }

            return when {
                isPm && hour != 12 -> hour + 12
                isPm.not() && hour == 12 -> 0
                else -> hour
            }
        }

        private fun buildLocalDateTime(resolvedHour: Int): VerdandiLocalDateTime {
            val nanoseconds = (millisecond * NANOS_PER_MILLI.toInt()).coerceIn(0, MAX_NANOSECOND)

            return VerdandiLocalDateTime.of(
                year = year,
                monthNumber = month,
                dayOfMonth = day,
                hour = resolvedHour,
                minute = minute,
                second = second,
                nanosecond = nanoseconds
            )
        }
    }
}
