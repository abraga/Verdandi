package com.github.abraga.verdandi.internal.region.locale

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
internal data class VerdandiLocale(
    val language: String,
    val country: String,
    val displayName: String,
    val nativeDisplayName: String,
    val script: String,
    val isRightToLeft: Boolean,
    val firstDayOfWeek: Int,
    val numberFormat: NumberFormat,
    val currency: Currency
) {

    @Transient
    val identifier: String = buildIdentifier()

    private fun buildIdentifier(): String {
        return buildString {
            append(language)

            if (country.isNotEmpty()) {
                append('_')
                append(country)
            }
        }
    }

    @Serializable
    @ConsistentCopyVisibility
    data class NumberFormat internal constructor(
        val decimalSeparator: String,
        val groupingSeparator: String
    )

    @Serializable
    @ConsistentCopyVisibility
    data class Currency internal constructor(
        val code: String,
        val symbol: String
    ) {

        @Transient
        val isAvailable: Boolean = code.isNotEmpty()
    }
}
