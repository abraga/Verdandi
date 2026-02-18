package com.github.abraga.verdandi.api.scope

import com.github.abraga.verdandi.internal.core.grammar.Plural
import com.github.abraga.verdandi.internal.core.grammar.Singular

interface TemporalUnitProvider {

    val millisecond: TemporalUnit<Singular>
        get() = TemporalUnit.Millisecond

    val second: TemporalUnit<Singular>
        get() = TemporalUnit.Second

    val minute: TemporalUnit<Singular>
        get() = TemporalUnit.Minute

    val hour: TemporalUnit<Singular>
        get() = TemporalUnit.Hour

    val day: TemporalUnit<Singular>
        get() = TemporalUnit.Day

    val week: TemporalUnit<Singular>
        get() = TemporalUnit.Week

    val month: TemporalUnit<Singular>
        get() = TemporalUnit.Month

    val year: TemporalUnit<Singular>
        get() = TemporalUnit.Year

    val milliseconds: TemporalUnit<Plural>
        get() = TemporalUnit.Milliseconds

    val seconds: TemporalUnit<Plural>
        get() = TemporalUnit.Seconds

    val minutes: TemporalUnit<Plural>
        get() = TemporalUnit.Minutes

    val hours: TemporalUnit<Plural>
        get() = TemporalUnit.Hours

    val days: TemporalUnit<Plural>
        get() = TemporalUnit.Days

    val weeks: TemporalUnit<Plural>
        get() = TemporalUnit.Weeks

    val months: TemporalUnit<Plural>
        get() = TemporalUnit.Months

    val years: TemporalUnit<Plural>
        get() = TemporalUnit.Years
}
