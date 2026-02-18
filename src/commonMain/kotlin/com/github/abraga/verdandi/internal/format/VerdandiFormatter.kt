package com.github.abraga.verdandi.internal.format

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.api.scope.format.VerdandiFormatScope
import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiFormatPattern

internal class VerdandiFormatter private constructor() {

    companion object {

        operator fun invoke(
            component: VerdandiMomentComponent,
            block: VerdandiFormatScope.() -> VerdandiFormatPattern
        ): String {
            val scope = VerdandiFormatScope()
            val result = scope.block()
            val segments = scope.build(result)

            return segments.joinToString("") { it.render(component) }
        }
    }
}
