@file:Suppress("PropertyName", "NonAsciiCharacters")

package com.github.abraga.verdandi.internal.format.chain

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiDatePattern
import com.github.abraga.verdandi.api.scope.format.chain.VerdandiDateTokenChain
import com.github.abraga.verdandi.internal.format.pad
import com.github.abraga.verdandi.internal.format.segment.ExtractorSegment
import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import com.github.abraga.verdandi.internal.format.segment.LiteralSegment

internal class DateTokenChain(
    private val prefix: List<FormatSegment> = emptyList(),
    private val separator: String = ""
) : VerdandiDateTokenChain {

    override val yyyy: VerdandiDatePattern
        get() = chain { year.toString() }

    override val yy: VerdandiDatePattern
        get() = chain { year.short }

    override val MM: VerdandiDatePattern
        get() = chain { month.toString() }

    override val MMM: VerdandiDatePattern
        get() = chain { month.shortName }

    override val MMMM: VerdandiDatePattern
        get() = chain { month.name }

    override val dd: VerdandiDatePattern
        get() = chain { day.toString() }

    override val d: VerdandiDatePattern
        get() = chain { day.value.toString() }

    override val E: VerdandiDatePattern
        get() = chain { dayOfWeek.value.toString() }

    override val EEE: VerdandiDatePattern
        get() = chain { dayOfWeekNameShort }

    override val EEEE: VerdandiDatePattern
        get() = chain { dayOfWeekName }

    override val Q: VerdandiDatePattern
        get() = chain { month.quarter.name }

    private fun chain(extractor: VerdandiMomentComponent.() -> String): VerdandiDatePattern {
        return VerdandiDatePattern(prefix + LiteralSegment(separator) + ExtractorSegment(extractor))
    }
}
