@file:Suppress("NonAsciiCharacters", "FunctionName", "SpellCheckingInspection", "unused")

package com.github.abraga.verdandi.api.scope.format.pattern

import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import com.github.abraga.verdandi.internal.format.segment.LiteralSegment
import kotlin.collections.plus

sealed interface VerdandiFormatPattern {

    fun getSegments(): List<FormatSegment>

    infix fun comma(other: VerdandiFormatPattern): VerdandiFormatPattern {
        return createLiteral(", ", other)
    }

    infix fun colon(other: VerdandiFormatPattern): VerdandiFormatPattern {
        return createLiteral(":", other)
    }

    infix fun at(other: VerdandiTimePattern): VerdandiFormatPattern {
        return createLiteral("at", other, true)
    }

    infix fun space(other: VerdandiFormatPattern): VerdandiFormatPattern {
        return createLiteral(" ", other)
    }

    infix fun T(other: VerdandiFormatPattern): VerdandiFormatPattern {
        return createLiteral("T", other)
    }

    operator fun div(other: VerdandiDatePattern): VerdandiDatePattern {
        return VerdandiDatePattern(getSegments() + LiteralSegment("/") + other.getSegments())
    }

    operator fun minus(other: VerdandiDatePattern): VerdandiDatePattern {
        return VerdandiDatePattern(getSegments() + LiteralSegment("-") + other.getSegments())
    }

    operator fun plus(literal: Char): VerdandiFormatPattern {
        return plus(literal.toString())
    }

    operator fun plus(literal: String): VerdandiFormatPattern {
        return VerdandiLiteralPattern(getSegments() + LiteralSegment(literal))
    }

    operator fun plus(pattern: VerdandiDatePattern): VerdandiFormatPattern {
        return VerdandiLiteralPattern(getSegments() + pattern.getSegments())
    }

    operator fun plus(pattern: VerdandiTimePattern): VerdandiFormatPattern {
        return VerdandiLiteralPattern(getSegments() + pattern.getSegments())
    }

    operator fun rangeTo(pattern: VerdandiFormatPattern): VerdandiFormatPattern {
        return space(pattern)
    }

    private fun createLiteral(
        word: String,
        other: VerdandiFormatPattern,
        spaced: Boolean = false
    ): VerdandiLiteralPattern {
        val literal = if (spaced) LiteralSegment(" $word ") else LiteralSegment(word)
        val combined = getSegments() + literal + other.getSegments()

        return VerdandiLiteralPattern(combined)
    }
}
