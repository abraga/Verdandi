@file:OptIn(ExperimentalAtomicApi::class)

package com.github.abraga.verdandi.internal.format.string

import com.github.abraga.verdandi.api.model.component.VerdandiMomentComponent
import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import kotlin.concurrent.atomics.AtomicReference
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.update

internal class StringPatternFormatter private constructor() {

    companion object {

        private val cache = AtomicReference<Map<String, List<FormatSegment>>>(emptyMap())

        operator fun invoke(component: VerdandiMomentComponent, pattern: String): String {
            val segments = resolveSegments(pattern)

            return segments.joinToString("") { segment ->
                segment.render(component)
            }
        }

        private fun resolveSegments(pattern: String): List<FormatSegment> {
            val currentCache = cache.load()
            val existing = currentCache[pattern]

            if (existing != null) {
                return existing
            }

            val tokens = FormatPatternTokenizer(pattern)
            val compiled = FormatPatternCompiler(tokens)

            cache.update { currentCache + (pattern to compiled) }

            return compiled
        }
    }
}
