package com.github.abraga.verdandi.internal.duration

internal object DateTimeDurationFormatter {

    fun format(
        components: List<Pair<Int, String>>,
        unitSeparator: String = "",
        componentSeparator: String = " ",
        skipZeros: Boolean = true
    ): String {
        return components.filter { (value, _) ->
            skipZeros.not() || value != 0
        }.joinToString(componentSeparator) { (value, unit) ->
            "$value$unitSeparator$unit"
        }
    }
}
