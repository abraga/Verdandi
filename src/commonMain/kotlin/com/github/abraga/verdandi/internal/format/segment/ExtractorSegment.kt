package com.github.abraga.verdandi.internal.format.segment

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import kotlin.jvm.JvmInline

@JvmInline
internal value class ExtractorSegment(
    private val extractor: (VerdandiMomentComponent) -> String
) : FormatSegment {

    override fun render(component: VerdandiMomentComponent): String {
        return extractor(component)
    }
}
