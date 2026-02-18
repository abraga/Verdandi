@file:Suppress("PropertyName")

package com.github.abraga.verdandi.api.scope.format.chain

import com.github.abraga.verdandi.api.scope.format.pattern.VerdandiTimePattern

interface VerdandiTimeTokenChain {

    val HH: VerdandiTimePattern

    val hh: VerdandiTimePattern

    val mm: VerdandiTimePattern

    val ss: VerdandiTimePattern

    val SSS: VerdandiTimePattern

    val A: VerdandiTimePattern

    val Z: VerdandiTimePattern
}
