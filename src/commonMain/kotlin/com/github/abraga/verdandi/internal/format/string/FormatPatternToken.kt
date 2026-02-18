package com.github.abraga.verdandi.internal.format.string

import kotlin.jvm.JvmInline

internal sealed interface FormatPatternToken {

    @JvmInline
    value class DirectiveToken(val pattern: String) : FormatPatternToken

    @JvmInline
    value class LiteralToken(val text: String) : FormatPatternToken
}
