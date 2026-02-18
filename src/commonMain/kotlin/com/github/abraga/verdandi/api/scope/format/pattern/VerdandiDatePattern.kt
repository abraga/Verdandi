package com.github.abraga.verdandi.api.scope.format.pattern

import com.github.abraga.verdandi.api.scope.format.chain.VerdandiDateTokenChain
import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import com.github.abraga.verdandi.internal.format.chain.DateTokenChain

class VerdandiDatePattern internal constructor(
    internal val segments: List<FormatSegment>
) : VerdandiFormatPattern, VerdandiDateTokenChain by DateTokenChain(segments, ".") {

    override fun getSegments(): List<FormatSegment> {
        return segments
    }
}
