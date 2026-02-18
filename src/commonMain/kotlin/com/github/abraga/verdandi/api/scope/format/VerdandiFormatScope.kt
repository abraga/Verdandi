package com.github.abraga.verdandi.api.scope.format

import com.github.abraga.verdandi.api.scope.format.chain.VerdandiDateTokenChain
import com.github.abraga.verdandi.api.scope.format.chain.VerdandiTimeTokenChain
import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiFormatPattern
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import com.github.abraga.verdandi.internal.format.chain.DateTokenChain
import com.github.abraga.verdandi.internal.format.chain.TimeTokenChain
import com.github.abraga.verdandi.internal.format.segment.FormatSegment

@VerdandiDslMarker
class VerdandiFormatScope internal constructor() :
    VerdandiDateTokenChain by DateTokenChain(), VerdandiTimeTokenChain by TimeTokenChain() {

    internal fun build(result: VerdandiFormatPattern): List<FormatSegment> {
        return result.getSegments()
    }
}
