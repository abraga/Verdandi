package com.github.abraga.verdandi.api.scope.adjust

import com.github.abraga.verdandi.api.scope.TemporalUnit
import com.github.abraga.verdandi.internal.core.grammar.Singular

interface AlignmentSelector {

    infix fun startOf(unit: TemporalUnit<Singular>)

    infix fun endOf(unit: TemporalUnit<Singular>)
}
