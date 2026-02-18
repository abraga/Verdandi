package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.internal.format.pad
import kotlin.test.Test
import kotlin.test.assertEquals

class FormatExtensionsTest {

    @Test
    fun `pad should zero-pad single digit to two digits by default`() {
        val inputValue = 5
        val expectedResult = "05"

        val actualResult = inputValue.pad()

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should not change two digit number`() {
        val inputValue = 15
        val expectedResult = "15"

        val actualResult = inputValue.pad()

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should use custom length`() {
        val inputValue = 5
        val customLength = 4
        val expectedResult = "0005"

        val actualResult = inputValue.pad(customLength)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should use custom padding character`() {
        val inputValue = 5
        val customLength = 4
        val customPadChar = ' '
        val expectedResult = "   5"

        val actualResult = inputValue.pad(customLength, customPadChar)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should handle zero value`() {
        val inputValue = 0
        val expectedResult = "00"

        val actualResult = inputValue.pad()

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should handle value larger than pad length`() {
        val inputValue = 123
        val expectedResult = "123"

        val actualResult = inputValue.pad()

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should handle single digit year padding to four digits`() {
        val inputValue = 5
        val yearPadLength = 4
        val expectedResult = "0005"

        val actualResult = inputValue.pad(yearPadLength)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `pad should handle milliseconds padding to three digits`() {
        val inputValue = 7
        val millisPadLength = 3
        val expectedResult = "007"

        val actualResult = inputValue.pad(millisPadLength)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }
}

