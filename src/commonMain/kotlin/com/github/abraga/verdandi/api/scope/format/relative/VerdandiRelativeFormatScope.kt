package com.github.abraga.verdandi.api.scope.format.relative

import com.github.abraga.verdandi.api.model.relative.Future
import com.github.abraga.verdandi.api.model.relative.VerdandiRelativeComponents
import com.github.abraga.verdandi.api.model.relative.VerdandiRelativeMoment
import com.github.abraga.verdandi.api.model.relative.VerdandiRelativeUnit
import com.github.abraga.verdandi.api.model.relative.Now
import com.github.abraga.verdandi.api.model.relative.Past
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import com.github.abraga.verdandi.internal.extension.verdandiRequire

@VerdandiDslMarker
class VerdandiRelativeFormatScope internal constructor(
    internal val labelResolver: ((VerdandiRelativeUnit) -> String)
) {

    var maxUnits: Int = 3
    var separator: String = ", "
    var lastSeparator: String = " and "

    private var nowResolver: (() -> String)? = null
    private var pastResolver: ((String) -> String)? = null
    private var futureResolver: ((String) -> String)? = null

    fun onNow(resolver: () -> String) {
        nowResolver = resolver
    }

    fun onPast(resolver: (String) -> String) {
        pastResolver = resolver
    }

    fun onFuture(resolver: (String) -> String) {
        futureResolver = resolver
    }

    internal fun build(moment: VerdandiRelativeMoment): String {
        return when (moment) {
            Now -> {
                val resolver = nowResolver

                verdandiRequire(
                    resolver != null,
                    { "now { } block is required" }
                )

                resolver()
            }
            is Past -> {
                val resolver = pastResolver

                verdandiRequire(
                    resolver != null,
                    { "past { } block is required" }
                )

                formatComponents(moment.components, resolver)
            }
            is Future -> {
                val resolver = futureResolver

                verdandiRequire(
                    resolver != null,
                    { "future { } block is required" }
                )

                formatComponents(moment.components, resolver)
            }
        }
    }

    private fun formatComponents(
        components: VerdandiRelativeComponents,
        resolver: (String) -> String
    ): String {
        val formatted = components.format(maxUnits, separator, lastSeparator, labelResolver)

        return resolver(formatted)
    }
}
