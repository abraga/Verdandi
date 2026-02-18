@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.locale

import java.text.DecimalFormatSymbols
import java.util.Calendar
import java.util.Currency
import java.util.Locale

internal actual class CurrentLocale actual constructor(value: String) {

    private val locale: Locale
        get() = Locale.getDefault()

    actual val value: String
        get() = locale.toString()

    actual val displayName: String
        get() = locale.getDisplayName(Locale.getDefault())

    actual val nativeDisplayName: String
        get() = locale.getDisplayName(locale)

    actual val script: String
        get() = locale.script

    actual val isRightToLeft: Boolean
        get() {
            val language = locale.language

            return language == "ar" || language == "he" || language == "fa" ||
                language == "ur" || language == "yi" || language == "ji"
        }

    actual val firstDayOfWeek: Int
        get() = Calendar.getInstance(locale).firstDayOfWeek - 1

    actual val decimalSeparator: String
        get() = DecimalFormatSymbols.getInstance(locale).decimalSeparator.toString()

    actual val groupingSeparator: String
        get() = DecimalFormatSymbols.getInstance(locale).groupingSeparator.toString()

    actual val currencyCode: String
        get() {
            return try {
                Currency.getInstance(locale).currencyCode
            } catch (_: IllegalArgumentException) {
                ""
            }
        }

    actual val currencySymbol: String
        get() {
            return try {
                Currency.getInstance(locale).symbol
            } catch (_: IllegalArgumentException) {
                ""
            }
        }
}
