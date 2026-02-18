@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.github.abraga.verdandi.api.scope.adjust

import com.github.abraga.verdandi.api.model.timezone.VerdandiTimeZone
import com.github.abraga.verdandi.api.scope.VerdandiDslMarker
import com.github.abraga.verdandi.api.scope.QuantityUnitSelector
import com.github.abraga.verdandi.api.model.duration.DateDuration
import kotlin.time.Duration

@VerdandiDslMarker
interface VerdandiAdjustScope : QuantityUnitSelector<Unit> {

    val at: AlignmentSelector

    val add: QuantityUnitSelector<Unit>

    val subtract: QuantityUnitSelector<Unit>

    var timeZone: VerdandiTimeZone?

    var weekStartsOn: WeekStart

    operator fun DateDuration.unaryPlus(): DateTimeDurationChain

    operator fun DateDuration.unaryMinus(): DateTimeDurationChain

    operator fun Duration.unaryPlus(): DateTimeDurationChain

    operator fun Duration.unaryMinus(): DateTimeDurationChain

    fun atYear(value: Int)

    fun atMonth(value: Int)

    fun atDay(value: Int)

    fun atHour(value: Int)

    fun atMinute(value: Int)

    fun atSecond(value: Int)
}
