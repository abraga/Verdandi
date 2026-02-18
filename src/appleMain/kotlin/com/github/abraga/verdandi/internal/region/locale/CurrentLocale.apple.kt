@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.locale

import platform.Foundation.NSCalendar
import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleCountryCode
import platform.Foundation.NSLocaleCurrencyCode
import platform.Foundation.NSLocaleCurrencySymbol
import platform.Foundation.NSLocaleDecimalSeparator
import platform.Foundation.NSLocaleGroupingSeparator
import platform.Foundation.NSLocaleIdentifier
import platform.Foundation.NSLocaleLanguageCode
import platform.Foundation.NSLocaleLanguageDirectionRightToLeft
import platform.Foundation.NSLocaleScriptCode
import platform.Foundation.characterDirectionForLanguage
import platform.Foundation.currentLocale
import platform.Foundation.localeIdentifier

internal actual class CurrentLocale actual constructor(value: String) {

    private val locale: NSLocale
        get() = NSLocale.currentLocale

    private val calendar: NSCalendar
        get() = NSCalendar.currentCalendar

    actual val value: String
        get() {
            val language = locale.objectForKey(NSLocaleLanguageCode) as? String ?: ""
            val country = locale.objectForKey(NSLocaleCountryCode) as? String ?: ""

            return if (country.isNotEmpty()) {
                "${language}_${country}"
            } else {
                language
            }
        }

    actual val displayName: String
        get() = locale.displayNameForKey(NSLocaleIdentifier, locale.localeIdentifier) ?: ""

    actual val nativeDisplayName: String
        get() {
            val targetLocale = NSLocale(locale.localeIdentifier)

            return targetLocale.displayNameForKey(NSLocaleIdentifier, locale.localeIdentifier) ?: ""
        }

    actual val script: String
        get() = locale.objectForKey(NSLocaleScriptCode) as? String ?: ""

    actual val isRightToLeft: Boolean
        get() {
            val languageCode = locale.objectForKey(NSLocaleLanguageCode) as? String ?: ""

            return NSLocale.characterDirectionForLanguage(languageCode) == NSLocaleLanguageDirectionRightToLeft
        }

    actual val firstDayOfWeek: Int
        get() = (calendar.firstWeekday.toInt() - 1) % 7

    actual val decimalSeparator: String
        get() = locale.objectForKey(NSLocaleDecimalSeparator) as? String ?: "."

    actual val groupingSeparator: String
        get() = locale.objectForKey(NSLocaleGroupingSeparator) as? String ?: ","

    actual val currencyCode: String
        get() = locale.objectForKey(NSLocaleCurrencyCode) as? String ?: ""

    actual val currencySymbol: String
        get() = locale.objectForKey(NSLocaleCurrencySymbol) as? String ?: ""
}
