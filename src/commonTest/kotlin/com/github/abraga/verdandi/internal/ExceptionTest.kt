package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.api.exception.VerdandiException
import com.github.abraga.verdandi.internal.extension.toVerdandi
import kotlin.test.Test
import kotlin.test.assertEquals

class ExceptionTest {

    @Test
    fun `toVerdandi should return same exception if already VerdandiException`() {
        val originalMessage = "Original Verdandi error"
        val originalException = VerdandiException(originalMessage)

        val result = originalException.toVerdandi()

        assertEquals(originalException, result, "Should return the same VerdandiException instance.")
        assertEquals(originalMessage, result.message, "Message should be preserved.")
    }

    @Test
    fun `toVerdandi should wrap non-VerdandiException with message`() {
        val originalMessage = "Some other error"
        val originalException = IllegalArgumentException(originalMessage)

        val result = originalException.toVerdandi()

        assertEquals(originalMessage, result.message, "Message should be preserved from original exception.")
    }

    @Test
    fun `toVerdandi should use default message when original has null message`() {
        val originalException = RuntimeException()

        val result = originalException.toVerdandi()

        assertEquals("An unexpected error occurred.", result.message, "Should use default message.")
    }

    @Test
    fun `toVerdandi should handle empty message`() {
        val originalException = IllegalStateException("")

        val result = originalException.toVerdandi()

        assertEquals("", result.message, "Empty message should be preserved.")
    }

    @Test
    fun `VerdandiException should store message correctly`() {
        val expectedMessage = "Test error message"
        val exception = VerdandiException(expectedMessage)

        assertEquals(expectedMessage, exception.message, "Message should be stored correctly.")
    }
}



