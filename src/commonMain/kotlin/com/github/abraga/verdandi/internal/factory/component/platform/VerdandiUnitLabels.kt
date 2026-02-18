package com.github.abraga.verdandi.internal.factory.component.platform

import com.github.abraga.verdandi.internal.region.locale.VerdandiLocale

internal object VerdandiUnitLabels {

    private val languageToLabels: Map<String, LocalizedUnitLabels> = mapOf(
        "en" to ENGLISH,
        "pt" to PORTUGUESE,
        "fr" to FRENCH,
        "it" to ITALIAN,
        "de" to GERMAN,
        "es" to SPANISH,
        "el" to GREEK,
        "ja" to JAPANESE,
        "pl" to POLISH,
        "ru" to RUSSIAN,
        "tr" to TURKISH,
        "ko" to KOREAN
    )

    private val chineseTraditionalCountries: Set<String> = setOf("TW", "HK", "MO")

    internal fun get(locale: VerdandiLocale): LocalizedUnitLabels {
        return resolveLabels(locale.language, locale.country, locale.identifier)
    }

    private fun resolveLabels(
        language: String,
        country: String,
        identifier: String
    ): LocalizedUnitLabels {
        if (language == "zh") {
            return resolveChineseVariant(country, identifier)
        }

        return languageToLabels[language] ?: ENGLISH
    }

    private fun resolveChineseVariant(country: String, identifier: String): LocalizedUnitLabels {
        if (country in chineseTraditionalCountries) {
            return CHINESE_TRADITIONAL
        }

        if (identifier.contains("Hant")) {
            return CHINESE_TRADITIONAL
        }

        return CHINESE_SIMPLIFIED
    }
}

internal data class LocalizedForm(
    val singular: String,
    val plural: String
)

internal data class LocalizedUnitLabels(
    val second: LocalizedForm,
    val minute: LocalizedForm,
    val hour: LocalizedForm,
    val day: LocalizedForm,
    val week: LocalizedForm,
    val month: LocalizedForm,
    val year: LocalizedForm
)

private val PORTUGUESE = LocalizedUnitLabels(
    second = LocalizedForm("segundo", "segundos"),
    minute = LocalizedForm("minuto", "minutos"),
    hour = LocalizedForm("hora", "horas"),
    day = LocalizedForm("dia", "dias"),
    week = LocalizedForm("semana", "semanas"),
    month = LocalizedForm("mês", "meses"),
    year = LocalizedForm("ano", "anos")
)

private val ENGLISH = LocalizedUnitLabels(
    second = LocalizedForm("second", "seconds"),
    minute = LocalizedForm("minute", "minutes"),
    hour = LocalizedForm("hour", "hours"),
    day = LocalizedForm("day", "days"),
    week = LocalizedForm("week", "weeks"),
    month = LocalizedForm("month", "months"),
    year = LocalizedForm("year", "years")
)

private val FRENCH = LocalizedUnitLabels(
    second = LocalizedForm("seconde", "secondes"),
    minute = LocalizedForm("minute", "minutes"),
    hour = LocalizedForm("heure", "heures"),
    day = LocalizedForm("jour", "jours"),
    week = LocalizedForm("semaine", "semaines"),
    month = LocalizedForm("mois", "mois"),
    year = LocalizedForm("an", "ans")
)

private val ITALIAN = LocalizedUnitLabels(
    second = LocalizedForm("secondo", "secondi"),
    minute = LocalizedForm("minuto", "minuti"),
    hour = LocalizedForm("ora", "ore"),
    day = LocalizedForm("giorno", "giorni"),
    week = LocalizedForm("settimana", "settimane"),
    month = LocalizedForm("mese", "mesi"),
    year = LocalizedForm("anno", "anni")
)

private val GERMAN = LocalizedUnitLabels(
    second = LocalizedForm("Sekunde", "Sekunden"),
    minute = LocalizedForm("Minute", "Minuten"),
    hour = LocalizedForm("Stunde", "Stunden"),
    day = LocalizedForm("Tag", "Tage"),
    week = LocalizedForm("Woche", "Wochen"),
    month = LocalizedForm("Monat", "Monate"),
    year = LocalizedForm("Jahr", "Jahre")
)

private val SPANISH = LocalizedUnitLabels(
    second = LocalizedForm("segundo", "segundos"),
    minute = LocalizedForm("minuto", "minutos"),
    hour = LocalizedForm("hora", "horas"),
    day = LocalizedForm("día", "días"),
    week = LocalizedForm("semana", "semanas"),
    month = LocalizedForm("mes", "meses"),
    year = LocalizedForm("año", "años")
)

private val GREEK = LocalizedUnitLabels(
    second = LocalizedForm("δευτερόλεπτο", "δευτερόλεπτα"),
    minute = LocalizedForm("λεπτό", "λεπτά"),
    hour = LocalizedForm("ώρα", "ώρες"),
    day = LocalizedForm("ημέρα", "ημέρες"),
    week = LocalizedForm("εβδομάδα", "εβδομάδες"),
    month = LocalizedForm("μήνας", "μήνες"),
    year = LocalizedForm("έτος", "έτη")
)

private val JAPANESE = LocalizedUnitLabels(
    second = LocalizedForm("秒", "秒"),
    minute = LocalizedForm("分", "分"),
    hour = LocalizedForm("時間", "時間"),
    day = LocalizedForm("日", "日"),
    week = LocalizedForm("週間", "週間"),
    month = LocalizedForm("か月", "か月"),
    year = LocalizedForm("年", "年")
)

private val POLISH = LocalizedUnitLabels(
    second = LocalizedForm("sekunda", "sekundy"),
    minute = LocalizedForm("minuta", "minuty"),
    hour = LocalizedForm("godzina", "godziny"),
    day = LocalizedForm("dzień", "dni"),
    week = LocalizedForm("tydzień", "tygodnie"),
    month = LocalizedForm("miesiąc", "miesiące"),
    year = LocalizedForm("rok", "lata")
)

private val RUSSIAN = LocalizedUnitLabels(
    second = LocalizedForm("секунда", "секунды"),
    minute = LocalizedForm("минута", "минуты"),
    hour = LocalizedForm("час", "часы"),
    day = LocalizedForm("день", "дни"),
    week = LocalizedForm("неделя", "недели"),
    month = LocalizedForm("месяц", "месяцы"),
    year = LocalizedForm("год", "годы")
)

private val TURKISH = LocalizedUnitLabels(
    second = LocalizedForm("saniye", "saniyeler"),
    minute = LocalizedForm("dakika", "dakikalar"),
    hour = LocalizedForm("saat", "saatler"),
    day = LocalizedForm("gün", "günler"),
    week = LocalizedForm("hafta", "haftalar"),
    month = LocalizedForm("ay", "aylar"),
    year = LocalizedForm("yıl", "yıllar")
)

private val CHINESE_SIMPLIFIED = LocalizedUnitLabels(
    second = LocalizedForm("秒", "秒"),
    minute = LocalizedForm("分钟", "分钟"),
    hour = LocalizedForm("小时", "小时"),
    day = LocalizedForm("天", "天"),
    week = LocalizedForm("周", "周"),
    month = LocalizedForm("个月", "个月"),
    year = LocalizedForm("年", "年")
)

private val CHINESE_TRADITIONAL = LocalizedUnitLabels(
    second = LocalizedForm("秒", "秒"),
    minute = LocalizedForm("分鐘", "分鐘"),
    hour = LocalizedForm("小時", "小時"),
    day = LocalizedForm("天", "天"),
    week = LocalizedForm("週", "週"),
    month = LocalizedForm("個月", "個月"),
    year = LocalizedForm("年", "年")
)

private val KOREAN = LocalizedUnitLabels(
    second = LocalizedForm("초", "초"),
    minute = LocalizedForm("분", "분"),
    hour = LocalizedForm("시간", "시간"),
    day = LocalizedForm("일", "일"),
    week = LocalizedForm("주", "주"),
    month = LocalizedForm("개월", "개월"),
    year = LocalizedForm("년", "년")
)
