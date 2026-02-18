package com.github.abraga.verdandi.internal.format.segment

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent

sealed interface FormatSegment {

    fun render(component: VerdandiMomentComponent): String
}
