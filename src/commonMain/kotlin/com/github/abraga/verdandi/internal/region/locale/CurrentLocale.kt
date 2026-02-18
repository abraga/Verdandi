@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.region.locale

internal expect class CurrentLocale(value: String) {

    val value: String

    val displayName: String

    val nativeDisplayName: String

    val script: String

    val isRightToLeft: Boolean

    val firstDayOfWeek: Int

    val decimalSeparator: String

    val groupingSeparator: String

    val currencyCode: String

    val currencySymbol: String
}
