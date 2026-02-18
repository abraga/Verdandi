package com.github.abraga.verdandi.internal.format.string

import com.github.abraga.verdandi.internal.extension.verdandiRequireParse

internal class FormatPatternTokenizer private constructor() {

    companion object {

        private val KNOWN_DIRECTIVES: Set<String> = setOf(
            "yyyy", "yy",
            "MMMM", "MMM", "MM",
            "dd", "d",
            "EEEE", "EEE",
            "HH", "hh",
            "mm",
            "ss",
            "SSS",
            "a",
            "Q",
            "Z"
        )

        operator fun invoke(pattern: String): List<FormatPatternToken> {
            verdandiRequireParse(
                pattern.isNotEmpty(),
                { "Format pattern must not be empty." }
            )

            var index = 0
            val tokens = mutableListOf<FormatPatternToken>()

            while (index < pattern.length) {
                val current = pattern[index]

                index = when {
                    current == '\'' -> {
                        consumeQuotedLiteral(pattern, index, tokens)
                    }
                    current.isLetter() -> {
                        consumeDirective(pattern, index, tokens)
                    }
                    else -> {
                        consumeLiteral(pattern, index, tokens)
                    }
                }
            }

            return tokens
        }

        private fun consumeQuotedLiteral(
            pattern: String,
            startIndex: Int,
            tokens: MutableList<FormatPatternToken>
        ): Int {
            val nextIndex = startIndex + 1

            if (nextIndex < pattern.length && pattern[nextIndex] == '\'') {
                tokens.add(FormatPatternToken.LiteralToken("'"))
                return nextIndex + 1
            }

            val closingIndex = pattern.indexOf('\'', nextIndex)

            verdandiRequireParse(
                closingIndex != -1,
                { "Unclosed quote in format pattern at position $startIndex." }
            )

            val content = pattern.substring(nextIndex, closingIndex)

            tokens.add(FormatPatternToken.LiteralToken(content))

            return closingIndex + 1
        }

        private fun consumeDirective(
            pattern: String,
            startIndex: Int,
            tokens: MutableList<FormatPatternToken>
        ): Int {
            val letter = pattern[startIndex]
            var endIndex = startIndex + 1

            while (endIndex < pattern.length && pattern[endIndex] == letter) {
                endIndex++
            }

            val group = pattern.substring(startIndex, endIndex)

            verdandiRequireParse(
                group in KNOWN_DIRECTIVES,
                { "Unrecognized format directive '$group' at position $startIndex." }
            )

            tokens.add(FormatPatternToken.DirectiveToken(group))

            return endIndex
        }

        private fun consumeLiteral(
            pattern: String,
            startIndex: Int,
            tokens: MutableList<FormatPatternToken>
        ): Int {
            var endIndex = startIndex + 1

            while (endIndex < pattern.length
                && pattern[endIndex].isLetter().not()
                && pattern[endIndex] != '\''
            ) {
                endIndex++
            }

            val text = pattern.substring(startIndex, endIndex)

            tokens.add(FormatPatternToken.LiteralToken(text))

            return endIndex
        }
    }
}
