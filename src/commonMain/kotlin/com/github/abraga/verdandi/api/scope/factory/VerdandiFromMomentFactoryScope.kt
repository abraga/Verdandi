package com.github.abraga.verdandi.api.scope.factory

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.model.VerdandiMoment
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker

@VerdandiDslMarker
interface VerdandiFromMomentFactoryScope {

    infix fun from(moment: VerdandiMoment): VerdandiInterval
}
