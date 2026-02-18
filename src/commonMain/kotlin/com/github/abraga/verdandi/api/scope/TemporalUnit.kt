package com.github.abraga.verdandi.api.scope

import com.github.abraga.verdandi.internal.core.grammar.GrammaticalNumber
import com.github.abraga.verdandi.internal.core.grammar.Plural
import com.github.abraga.verdandi.internal.core.grammar.Singular
import kotlinx.serialization.Serializable

@Serializable
sealed class TemporalUnit<out N : GrammaticalNumber>(internal val id: Int) {

    data object Millisecond : TemporalUnit<Singular>(0)
    data object Second : TemporalUnit<Singular>(1)
    data object Minute : TemporalUnit<Singular>(2)
    data object Hour : TemporalUnit<Singular>(3)
    data object Day : TemporalUnit<Singular>(4)
    data object Week : TemporalUnit<Singular>(5)
    data object Month : TemporalUnit<Singular>(6)
    data object Year : TemporalUnit<Singular>(7)

    data object Milliseconds : TemporalUnit<Plural>(0)
    data object Seconds : TemporalUnit<Plural>(1)
    data object Minutes : TemporalUnit<Plural>(2)
    data object Hours : TemporalUnit<Plural>(3)
    data object Days : TemporalUnit<Plural>(4)
    data object Weeks : TemporalUnit<Plural>(5)
    data object Months : TemporalUnit<Plural>(6)
    data object Years : TemporalUnit<Plural>(7)
}
