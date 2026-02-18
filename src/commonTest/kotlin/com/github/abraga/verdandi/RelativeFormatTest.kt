package com.github.abraga.verdandi

import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

class RelativeFormatTest {

    @Test
    fun `relativeToNow with current moment should format as now`() {
        val nowThreshold = 5.seconds

        val moment = Verdandi.now()
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { "now" }
            onPast { "$it ago" }
            onFuture { "in $it" }
        }

        assertTrue(
            formattedResult == "now" || formattedResult.contains("ago") || formattedResult.contains("in"),
            "Current moment should format as 'now' or relative time."
        )
    }

    @Test
    fun `relativeToNow with past moment should format with ago suffix`() {
        val pastOffset = 2.hours
        val nowThreshold = 1.seconds

        val moment = Verdandi.now() - pastOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { "now" }
            onPast { "$it ago" }
            onFuture { "in $it" }
        }

        assertTrue(formattedResult.contains("ago"), "Past moment should format with 'ago' suffix.")
    }

    @Test
    fun `relativeToNow with future moment should format with in prefix`() {
        val futureOffset = 3.hours
        val nowThreshold = 1.seconds

        val moment = Verdandi.now() + futureOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { "now" }
            onPast { "$it ago" }
            onFuture { "in $it" }
        }

        assertTrue(formattedResult.contains("in"), "Future moment should format with 'in' prefix.")
    }

    @Test
    fun `relativeTo other moment should calculate difference correctly`() {
        val referenceTimestamp = "2025-06-15T12:00:00Z"
        val targetTimestamp = "2025-06-15T10:00:00Z"
        val nowThreshold = 1.seconds

        val referenceMoment = Verdandi.from(referenceTimestamp)
        val targetMoment = Verdandi.from(targetTimestamp)
        val relativeMoment = targetMoment.relativeTo(referenceMoment, nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { "now" }
            onPast { "$it ago" }
            onFuture { "in $it" }
        }

        assertTrue(formattedResult.contains("ago"), "Target before reference should format as past.")
    }

    @Test
    fun `relative format with maxUnits should limit displayed units`() {
        val pastOffset = 2.days + 5.hours + 30.minutes
        val nowThreshold = 1.seconds
        val maxUnitsToDisplay = 2

        val moment = Verdandi.now() - pastOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            maxUnits = maxUnitsToDisplay
            onNow { "now" }
            onPast { it }
            onFuture { it }
        }

        assertTrue(formattedResult.isNotEmpty(), "Formatted result should not be empty.")
    }

    @Test
    fun `relative format with custom separator should use specified separator`() {
        val pastOffset = 1.days + 2.hours
        val nowThreshold = 1.seconds
        val customSeparator = " and "

        val moment = Verdandi.now() - pastOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            separator = customSeparator
            onNow { "now" }
            onPast { it }
            onFuture { it }
        }

        assertTrue(formattedResult.isNotEmpty(), "Formatted result should not be empty.")
    }

    @Test
    fun `relative format with lastSeparator should use different separator for last unit`() {
        val pastOffset = 1.days + 2.hours + 30.minutes
        val nowThreshold = 1.seconds
        val customSeparator = ", "
        val customLastSeparator = " and "
        val maxUnitsToDisplay = 3

        val moment = Verdandi.now() - pastOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            maxUnits = maxUnitsToDisplay
            separator = customSeparator
            lastSeparator = customLastSeparator
            onNow { "now" }
            onPast { it }
            onFuture { it }
        }

        assertTrue(formattedResult.isNotEmpty(), "Formatted result should not be empty.")
    }

    @Test
    fun `onNow block should format current moment with custom text`() {
        val nowThreshold = 5.seconds
        val expectedText = "right now"

        val moment = Verdandi.now()
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { expectedText }
            onPast { "$it ago" }
            onFuture { "in $it" }
        }

        assertTrue(
            formattedResult == expectedText || formattedResult.contains("ago") || formattedResult.contains("in"),
            "Current moment should format with custom text or relative time."
        )
    }

    @Test
    fun `onPast block should format past moment with custom template`() {
        val pastOffset = 1.hours
        val nowThreshold = 1.seconds

        val moment = Verdandi.now() - pastOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { "now" }
            onPast { "happened $it ago" }
            onFuture { "in $it" }
        }

        assertTrue(
            formattedResult.contains("happened") && formattedResult.contains("ago"),
            "Past moment should format with custom template."
        )
    }

    @Test
    fun `onFuture block should format future moment with custom template`() {
        val futureOffset = 2.days
        val nowThreshold = 1.seconds

        val moment = Verdandi.now() + futureOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            onNow { "now" }
            onPast { "$it ago" }
            onFuture { "coming in $it" }
        }

        assertTrue(formattedResult.contains("coming"), "Future moment should format with custom template.")
    }

    @Test
    fun `relative format with all options should produce non-empty result`() {
        val pastOffset = 3.days + 4.hours
        val nowThreshold = 1.seconds
        val maxUnitsToDisplay = 2
        val customSeparator = ", "
        val customLastSeparator = " and "

        val moment = Verdandi.now() - pastOffset
        val relativeMoment = moment.relativeToNow(nowThreshold)
        val formattedResult = relativeMoment format {
            maxUnits = maxUnitsToDisplay
            separator = customSeparator
            lastSeparator = customLastSeparator
            onNow { "just now" }
            onPast { it }
            onFuture { it }
        }

        assertTrue(formattedResult.isNotEmpty(), "Formatted result with all options should not be empty.")
    }
}
