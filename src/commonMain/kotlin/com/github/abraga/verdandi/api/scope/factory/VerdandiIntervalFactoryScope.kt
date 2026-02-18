package com.github.abraga.verdandi.api.scope.factory

import com.github.abraga.verdandi.api.model.VerdandiInterval
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import com.github.abraga.verdandi.api.scope.QuantityUnitSelector
import com.github.abraga.verdandi.api.scope.TemporalUnitProvider

@VerdandiDslMarker
interface VerdandiIntervalFactoryScope : TemporalUnitProvider {

    val last: QuantityUnitSelector<VerdandiInterval>

    val next: QuantityUnitSelector<VerdandiInterval>
}
