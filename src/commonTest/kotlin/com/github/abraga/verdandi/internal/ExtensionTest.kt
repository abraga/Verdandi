package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.api.exception.VerdandiException
import com.github.abraga.verdandi.internal.extension.verdandiError
import com.github.abraga.verdandi.internal.extension.verdandiRequire
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ExtensionTest {

    @Test
    fun `verdandiRequire should not throw when condition is true`() {
        var executed: Boolean

        verdandiRequire(true) { "Should not throw" }
        executed = true

        assertEquals(true, executed, "Code after verdandiRequire should execute when condition is true.")
    }

    @Test
    fun `verdandiRequire should throw VerdandiException when condition is false`() {
        val expectedMessage = "Validation failed"

        val exception = assertFailsWith<VerdandiException> {
            verdandiRequire(false) { expectedMessage }
        }

        assertEquals(expectedMessage, exception.message, "Exception message should match.")
    }

    @Test
    fun `verdandiRequire should use lazy message evaluation`() {
        var messageEvaluated = false

        verdandiRequire(true) {
            messageEvaluated = true
            "Should not evaluate"
        }

        assertEquals(false, messageEvaluated, "Message should not be evaluated when condition is true.")
    }

    @Test
    fun `verdandiError should throw VerdandiException with message`() {
        val expectedMessage = "Error occurred"

        val exception = assertFailsWith<VerdandiException> {
            verdandiError(expectedMessage)
        }

        assertEquals(expectedMessage, exception.message, "Exception message should match.")
    }

    @Test
    fun `verdandiError should never return`() {
        val result: String = try {
            verdandiError("This always throws")
        } catch (_: VerdandiException) {
            "caught"
        }

        assertEquals("caught", result, "verdandiError should always throw.")
    }
}



