package com.github.abraga.verdandi.internal.format.string

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.internal.extension.currentLocale
import com.github.abraga.verdandi.internal.factory.component.platform.getAmPmName
import com.github.abraga.verdandi.internal.format.pad
import com.github.abraga.verdandi.internal.format.segment.ExtractorSegment
import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import com.github.abraga.verdandi.internal.format.segment.LiteralSegment

internal class FormatPatternCompiler private constructor() {

    companion object {

        private val amPmNames: Pair<String, String> by lazy {
            val locale = currentLocale()

            getAmPmName(locale)
        }

        private val DIRECTIVE_EXTRACTORS: Map<String, (VerdandiMomentComponent) -> String> = mapOf(
            "yyyy" to { component -> component.year.toString() },
            "yy" to { component -> component.year.short },
            "MMMM" to { component -> component.month.name },
            "MMM" to { component -> component.month.shortName },
            "MM" to { component -> component.month.toString() },
            "dd" to { component -> component.day.toString() },
            "d" to { component -> component.day.value.toString() },
            "EEEE" to { component -> component.dayOfWeekName },
            "EEE" to { component -> component.dayOfWeekNameShort },
            "HH" to { component -> component.hour.toString() },
            "hh" to { component -> component.hour.valueIn12.pad() },
            "mm" to { component -> component.minute.toString() },
            "ss" to { component -> component.second.toString() },
            "SSS" to { component -> component.millisecond.toString() },
            "a" to { component ->
                if (component.hour.isAM) amPmNames.first else amPmNames.second
            },
            "Q" to { component -> component.month.quarter.name },
            "Z" to { component -> component.offset.toString() }
        )

        operator fun invoke(tokens: List<FormatPatternToken>): List<FormatSegment> {
            return tokens.map { token ->
                when (token) {
                    is FormatPatternToken.DirectiveToken -> {
                        val extractor = DIRECTIVE_EXTRACTORS.getValue(token.pattern)

                        ExtractorSegment(extractor)
                    }

                    is FormatPatternToken.LiteralToken -> {
                        LiteralSegment(token.text)
                    }
                }
            }
        }
    }
}
