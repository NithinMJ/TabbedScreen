package com.example.nithinjohn.tabbedscreen

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @ParameterizedTest
    @CsvSource(
            "1, 1",
            "2, 4",
            "3, 9"
    )
    fun testSquares(input: Int, expected: Int) {
        Assertions.assertEquals(expected, input * input)
    }
}
