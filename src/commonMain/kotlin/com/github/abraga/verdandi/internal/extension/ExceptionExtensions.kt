@file:OptIn(ExperimentalContracts::class)

package com.github.abraga.verdandi.internal.extension

import com.github.abraga.verdandi.api.exception.VerdandiConfigurationException
import com.github.abraga.verdandi.api.exception.VerdandiException
import com.github.abraga.verdandi.api.exception.VerdandiInputException
import com.github.abraga.verdandi.api.exception.VerdandiParseException
import com.github.abraga.verdandi.api.exception.VerdandiStateException
import com.github.abraga.verdandi.api.exception.VerdandiValidationException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

internal inline fun verdandiRequire(
    value: Boolean,
    lazyMessage: () -> String
) {
    contract {
        returns() implies value
    }

    if (value.not()) {
        throw VerdandiException(lazyMessage())
    }
}

internal inline fun verdandiRequireValidation(
    value: Boolean,
    lazyMessage: () -> String
) {
    contract {
        returns() implies value
    }

    if (value.not()) {
        throw VerdandiValidationException(lazyMessage())
    }
}

internal inline fun verdandiRequireParse(
    value: Boolean,
    lazyMessage: () -> String
) {
    contract {
        returns() implies value
    }

    if (value.not()) {
        throw VerdandiParseException(lazyMessage())
    }
}

internal fun verdandiError(message: String): Nothing {
    throw VerdandiException(message)
}

internal fun verdandiParseError(message: String): Nothing {
    throw VerdandiParseException(message)
}

internal fun verdandiInputError(message: String): Nothing {
    throw VerdandiInputException(message)
}

internal fun verdandiStateError(message: String): Nothing {
    throw VerdandiStateException(message)
}

internal fun verdandiConfigurationError(message: String): Nothing {
    throw VerdandiConfigurationException(message)
}

internal fun Throwable.toVerdandi(): VerdandiException {
    return this as? VerdandiException
        ?: VerdandiException(message ?: "An unexpected error occurred.", cause = this)
}

internal fun <T : Any> T.verdandiRequireRangeValidation(
    actualValue: Int,
    range: IntRange,
    className: String? = null
) {
    verdandiRequireValidation(actualValue in range) {
        val name = (className ?: this::class.simpleName).takeIf {
            it != null
        }?.let { "'$it'" } ?: "The"

        "$name value must be between ${range.first} and ${range.last}, but it was $actualValue."
    }
}
