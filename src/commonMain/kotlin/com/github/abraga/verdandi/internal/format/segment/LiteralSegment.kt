package com.github.abraga.verdandi.internal.format.segment

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import kotlin.jvm.JvmInline

@JvmInline
internal value class LiteralSegment(private val text: String) : FormatSegment {

    override fun render(component: VerdandiMomentComponent): String {
        return text
    }
}
