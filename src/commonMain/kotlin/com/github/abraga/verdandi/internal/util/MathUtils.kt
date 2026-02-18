package com.github.abraga.verdandi.internal.util

import com.github.abraga.verdandi.internal.extension.toVerdandi

internal object MathUtils {

    fun floorDiv(dividend: Long, divisor: Int): Long {
        return floorDiv(dividend, divisor.toLong())
    }

    fun floorDiv(dividend: Long, divisor: Long): Long {
        var result = dividend / divisor

        val hasNonZeroRemainder = (dividend % divisor) != 0L
        val signsAreDifferent = (dividend xor divisor) < 0

        if (hasNonZeroRemainder && signsAreDifferent) {
            result -= 1
        }

        return result
    }

    fun floorMod(dividend: Long, divisor: Int): Long {
        return floorMod(dividend, divisor.toLong())
    }

    fun floorMod(dividend: Long, divisor: Long): Long {
        return ((dividend % divisor) + divisor) % divisor
    }

    fun addExact(augend: Long, addend: Long): Long {
        val result = augend + addend

        if (((augend xor result) and (addend xor result)) < 0) {
            throw ArithmeticException("Long overflow: $augend + $addend").toVerdandi()
        }

        return result
    }

    fun subtractExact(minuend: Long, subtrahend: Long): Long {
        val result = minuend - subtrahend

        if (((minuend xor subtrahend) and (minuend xor result)) < 0) {
            throw ArithmeticException("Long overflow: $minuend - $subtrahend").toVerdandi()
        }

        return result
    }

    fun multiplyExact(multiplicand: Long, multiplier: Long): Long {
        val result = multiplicand * multiplier

        if (multiplicand != 0L && result / multiplicand != multiplier) {
            throw ArithmeticException("Long overflow: $multiplicand * $multiplier").toVerdandi()
        }

        return result
    }

    fun shrinkInterval(
        startMillis: Long,
        endMillis: Long,
        deltaMillis: Long
    ): Pair<Long, Long> {
        val newStart = startMillis + deltaMillis
        val newEnd = endMillis - deltaMillis

        return if (newStart >= newEnd) {
            val midpoint = startMillis + (endMillis - startMillis) / 2
            midpoint to midpoint
        } else {
            newStart to newEnd
        }
    }
}
