package com.github.abraga.verdandi.internal.format.chain

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.api.scope.format.chain.VerdandiTimeTokenChain
import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiTimePattern
import com.github.abraga.verdandi.internal.extension.currentLocale
import com.github.abraga.verdandi.internal.factory.component.platform.getAmPmName
import com.github.abraga.verdandi.internal.format.pad
import com.github.abraga.verdandi.internal.format.segment.ExtractorSegment
import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import com.github.abraga.verdandi.internal.format.segment.LiteralSegment
import kotlin.jvm.JvmName

internal class TimeTokenChain(
    private val prefix: List<FormatSegment> = emptyList(),
    private val separator: String = ""
) : VerdandiTimeTokenChain {

    private val amPmNames by lazy {
        val locale = currentLocale()

        getAmPmName(locale)
    }

    override val HH: VerdandiTimePattern
        get() = chain { hour.toString() }

    override val hh: VerdandiTimePattern
        get() = chain { hour.valueIn12.pad() }

    override val mm: VerdandiTimePattern
        get() = chain { minute.toString() }

    override val ss: VerdandiTimePattern
        get() = chain { second.toString() }

    override val SSS: VerdandiTimePattern
        get() = chain(separator = ".") { millisecond.toString() }

    override val A: VerdandiTimePattern
        get() = chain { if (hour.isAM) amPmNames.first else amPmNames.second }

    override val Z: VerdandiTimePattern
        get() = chain(separator = "") { offset.toString() }

    private fun chain(
        separator: String = this.separator,
        extractor: VerdandiMomentComponent.() -> String
    ): VerdandiTimePattern {
        return VerdandiTimePattern(prefix + LiteralSegment(separator) + ExtractorSegment(extractor))
    }
}
