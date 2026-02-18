@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package com.github.abraga.verdandi.internal.core

internal expect object VerdandiClock {

    fun now(): VerdandiInstant
}
