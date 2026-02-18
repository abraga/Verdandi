@file:Suppress("PropertyName", "NonAsciiCharacters")

package com.github.abraga.verdandi.api.scope.format.chain

import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiDatePattern

interface VerdandiDateTokenChain {

    val yyyy: VerdandiDatePattern

    val yy: VerdandiDatePattern

    val MM: VerdandiDatePattern

    val MMM: VerdandiDatePattern

    val MMMM: VerdandiDatePattern

    val dd: VerdandiDatePattern

    val d: VerdandiDatePattern

    val E: VerdandiDatePattern

    val EEE: VerdandiDatePattern

    val EEEE: VerdandiDatePattern

    val Q: VerdandiDatePattern
}
