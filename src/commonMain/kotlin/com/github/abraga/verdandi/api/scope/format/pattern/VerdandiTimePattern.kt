package com.github.abraga.verdandi.api.scope.format.pattern

import com.github.abraga.verdandi.api.scope.format.chain.VerdandiTimeTokenChain
import com.github.abraga.verdandi.internal.format.segment.FormatSegment
import com.github.abraga.verdandi.internal.format.chain.TimeTokenChain

class VerdandiTimePattern internal constructor(
    internal val segments: List<FormatSegment>
) : VerdandiFormatPattern, VerdandiTimeTokenChain by TimeTokenChain(segments, ":") {

    override fun getSegments(): List<FormatSegment> {
        return segments
    }
}
