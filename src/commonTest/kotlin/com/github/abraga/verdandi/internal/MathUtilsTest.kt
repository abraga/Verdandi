package com.github.abraga.verdandi.internal

import com.github.abraga.verdandi.api.exception.VerdandiException
import com.github.abraga.verdandi.internal.util.MathUtils
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MathUtilsTest {

    @Test
    fun `floorDiv should return correct result for positive dividend and divisor`() {
        val dividend = 10L
        val divisor = 3L
        val expectedResult = 3L

        val actualResult = MathUtils.floorDiv(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorDiv should round toward negative infinity for negative dividend`() {
        val dividend = -10L
        val divisor = 3L
        val expectedResult = -4L

        val actualResult = MathUtils.floorDiv(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorDiv should round toward negative infinity for negative divisor`() {
        val dividend = 10L
        val divisor = -3L
        val expectedResult = -4L

        val actualResult = MathUtils.floorDiv(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorDiv should return zero for zero dividend`() {
        val dividend = 0L
        val divisor = 5L
        val expectedResult = 0L

        val actualResult = MathUtils.floorDiv(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorDiv with int divisor should work correctly`() {
        val dividend = 17L
        val divisor = 5
        val expectedResult = 3L

        val actualResult = MathUtils.floorDiv(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorMod should return positive remainder for positive inputs`() {
        val dividend = 10L
        val divisor = 3L
        val expectedResult = 1L

        val actualResult = MathUtils.floorMod(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorMod should return positive remainder for negative dividend`() {
        val dividend = -10L
        val divisor = 3L
        val expectedResult = 2L

        val actualResult = MathUtils.floorMod(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorMod should return zero when dividend is exactly divisible`() {
        val dividend = 15L
        val divisor = 5L
        val expectedResult = 0L

        val actualResult = MathUtils.floorMod(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `floorMod with int divisor should work correctly`() {
        val dividend = 17L
        val divisor = 5
        val expectedResult = 2L

        val actualResult = MathUtils.floorMod(dividend, divisor)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `addExact should return correct sum for positive numbers`() {
        val augend = 100L
        val addend = 200L
        val expectedResult = 300L

        val actualResult = MathUtils.addExact(augend, addend)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `addExact should return correct sum for negative numbers`() {
        val augend = -100L
        val addend = -200L
        val expectedResult = -300L

        val actualResult = MathUtils.addExact(augend, addend)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `addExact should throw exception on overflow`() {
        val augend = Long.MAX_VALUE
        val addend = 1L

        assertFailsWith<VerdandiException> {
            MathUtils.addExact(augend, addend)
        }
    }

    @Test
    fun `subtractExact should return correct difference for positive numbers`() {
        val minuend = 300L
        val subtrahend = 100L
        val expectedResult = 200L

        val actualResult = MathUtils.subtractExact(minuend, subtrahend)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `subtractExact should handle negative result correctly`() {
        val minuend = 100L
        val subtrahend = 300L
        val expectedResult = -200L

        val actualResult = MathUtils.subtractExact(minuend, subtrahend)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `subtractExact should throw exception on underflow`() {
        val minuend = Long.MIN_VALUE
        val subtrahend = 1L

        assertFailsWith<VerdandiException> {
            MathUtils.subtractExact(minuend, subtrahend)
        }
    }

    @Test
    fun `multiplyExact should return correct product`() {
        val multiplicand = 10L
        val multiplier = 20L
        val expectedResult = 200L

        val actualResult = MathUtils.multiplyExact(multiplicand, multiplier)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `multiplyExact should throw exception on overflow`() {
        val multiplicand = Long.MAX_VALUE
        val multiplier = 2L

        assertFailsWith<VerdandiException> {
            MathUtils.multiplyExact(multiplicand, multiplier)
        }
    }

    @Test
    fun `multiplyExact should handle zero correctly`() {
        val multiplicand = 0L
        val multiplier = Long.MAX_VALUE
        val expectedResult = 0L

        val actualResult = MathUtils.multiplyExact(multiplicand, multiplier)

        assertEquals(expectedResult, actualResult, "Expected '$expectedResult', but got '$actualResult'.")
    }

    @Test
    fun `shrinkInterval should contract interval from both sides`() {
        val startMillis = 1000L
        val endMillis = 5000L
        val deltaMillis = 500L
        val expectedStart = 1500L
        val expectedEnd = 4500L

        val (actualStart, actualEnd) = MathUtils.shrinkInterval(startMillis, endMillis, deltaMillis)

        assertEquals(expectedStart, actualStart, "Expected start '$expectedStart', but got '$actualStart'.")
        assertEquals(expectedEnd, actualEnd, "Expected end '$expectedEnd', but got '$actualEnd'.")
    }

    @Test
    fun `shrinkInterval should return midpoint when delta exceeds half interval`() {
        val startMillis = 1000L
        val endMillis = 2000L
        val deltaMillis = 1000L
        val expectedMidpoint = 1500L

        val (actualStart, actualEnd) = MathUtils.shrinkInterval(startMillis, endMillis, deltaMillis)

        assertEquals(expectedMidpoint, actualStart, "Start should be midpoint when shrink exceeds interval.")
        assertEquals(expectedMidpoint, actualEnd, "End should be midpoint when shrink exceeds interval.")
    }

    @Test
    fun `shrinkInterval should return midpoint when new start would exceed new end`() {
        val startMillis = 1000L
        val endMillis = 2000L
        val deltaMillis = 600L
        val expectedMidpoint = 1500L

        val (actualStart, actualEnd) = MathUtils.shrinkInterval(startMillis, endMillis, deltaMillis)

        assertEquals(expectedMidpoint, actualStart, "Start should be midpoint when shrink causes overlap.")
        assertEquals(expectedMidpoint, actualEnd, "End should be midpoint when shrink causes overlap.")
    }
}

