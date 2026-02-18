@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.locale

internal actual class CurrentLocale actual constructor(value: String) {

    actual val value: String
        get() = jsGetLanguage().replace('-', '_')

    actual val displayName: String
        get() = jsGetDisplayName()

    actual val nativeDisplayName: String
        get() = jsGetNativeDisplayName()

    actual val script: String
        get() = jsGetScript()

    actual val isRightToLeft: Boolean
        get() {
            val language = jsGetLanguage().split('-').firstOrNull() ?: ""

            return language in RTL_LANGUAGES
        }

    actual val firstDayOfWeek: Int
        get() = jsGetFirstDayOfWeek()

    actual val decimalSeparator: String
        get() = jsGetDecimalSeparator()

    actual val groupingSeparator: String
        get() = jsGetGroupingSeparator()

    actual val currencyCode: String
        get() = jsGetCurrencyCode()

    actual val currencySymbol: String
        get() = jsGetCurrencySymbol()

    private companion object {

        private val RTL_LANGUAGES = setOf("ar", "he", "fa", "ur", "yi", "ji")
    }
}

@JsFun("() => navigator.language")
private external fun jsGetLanguage(): String

@JsFun(
    """
    () => {
        var lang = navigator.language;
        return new Intl.DisplayNames([lang], { type: 'language' }).of(lang.split('-')[0]);
    }
    """
)
private external fun jsGetDisplayName(): String

@JsFun(
    """
    () => {
        var langCode = navigator.language.split('-')[0];
        return new Intl.DisplayNames([langCode], { type: 'language' }).of(langCode);
    }
    """
)
private external fun jsGetNativeDisplayName(): String

@JsFun(
    """
    () => {
        var parts = navigator.language.split('-');
        return parts.length > 2 ? parts[1] : '';
    }
    """
)
private external fun jsGetScript(): String

@JsFun(
    """
    () => {
        var locale = navigator.language;
        var weekInfo = new Intl.Locale(locale).weekInfo || new Intl.Locale(locale).getWeekInfo();
        return weekInfo ? (weekInfo.firstDay || 1) - 1 : 0;
    }
    """
)
private external fun jsGetFirstDayOfWeek(): Int

@JsFun("() => (1.1).toLocaleString(navigator.language).charAt(1)")
private external fun jsGetDecimalSeparator(): String

@JsFun("() => (1000).toLocaleString(navigator.language).charAt(1)")
private external fun jsGetGroupingSeparator(): String

@JsFun(
    """
    () => {
        try {
            var parts = new Intl.NumberFormat(navigator.language, {style: 'currency', currency: 'USD'}).formatToParts(1);
            var currencyPart = parts.find(function(p) { return p.type === 'currency'; });
            return currencyPart ? currencyPart.value : '';
        } catch(e) {
            return '';
        }
    }
    """
)
private external fun jsGetCurrencyCode(): String

@JsFun(
    """
    () => {
        try {
            return new Intl.NumberFormat(navigator.language, {style: 'currency', currency: 'USD'}).format(0).replace(/[\d.,\s]/g, '');
        } catch(e) {
            return '';
        }
    }
    """
)
private external fun jsGetCurrencySymbol(): String
