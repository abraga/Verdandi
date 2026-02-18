package com.github.abraga.verdandi.api.scope.format.pattern

import com.github.abraga.verdandi.internal.format.segment.FormatSegment

class VerdandiLiteralPattern internal constructor(
    internal val segments: List<FormatSegment>
) : VerdandiFormatPattern {

    override fun getSegments(): List<FormatSegment> {
        return segments
    }
}
