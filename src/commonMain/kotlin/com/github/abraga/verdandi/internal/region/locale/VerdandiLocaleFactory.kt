package com.github.abraga.verdandi.internal.region.locale

internal class VerdandiLocaleFactory {

    fun current(): VerdandiLocale {
        val currentLocale = CurrentLocale("")

        return VerdandiLocale(
            language = extractLanguage(currentLocale.value),
            country = extractCountry(currentLocale.value),
            displayName = currentLocale.displayName,
            nativeDisplayName = currentLocale.nativeDisplayName,
            script = currentLocale.script,
            isRightToLeft = currentLocale.isRightToLeft,
            firstDayOfWeek = currentLocale.firstDayOfWeek,
            numberFormat = VerdandiLocale.NumberFormat(
                decimalSeparator = currentLocale.decimalSeparator,
                groupingSeparator = currentLocale.groupingSeparator
            ),
            currency = VerdandiLocale.Currency(
                code = currentLocale.currencyCode,
                symbol = currentLocale.currencySymbol
            )
        )
    }

    private fun extractLanguage(localeValue: String): String {
        return localeValue.split('_', '-').firstOrNull() ?: ""
    }

    private fun extractCountry(localeValue: String): String {
        val parts = localeValue.split('_', '-')

        return if (parts.size > 1) parts[1] else ""
    }
}
