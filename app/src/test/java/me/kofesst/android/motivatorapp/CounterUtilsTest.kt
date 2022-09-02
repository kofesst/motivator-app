package me.kofesst.android.motivatorapp

import me.kofesst.android.motivatorapp.presentation.utils.asCounter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * Unit тесты, проверяющие на корректность
 * преобразования числа в счетчик с помощью [Int.asCounter].
 */
class CounterUtilsTest {
    companion object {
        /**
         * Параметры для теста.
         */
        @JvmStatic
        fun testParameters(): Stream<Arguments> = Stream.of(
            Arguments.of(1, "1"),
            Arguments.of(999, "999"),
            Arguments.of(1_000, "1K"),
            Arguments.of(123_456, "123,4K"),
            Arguments.of(999_999, "999,9K"),
            Arguments.of(1_000_000, "1M"),
            Arguments.of(987_654_321, "987,6M"),
            Arguments.of(999_999_999, "999,9M"),
            Arguments.of(1_000_000_000, "1000M"),
        )
    }

    /**
     * Unit тест для функции [Int.asCounter].
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    fun `asCounter() works as expected`(value: Int, expected: String) {
        val actual = value.asCounter()
        assertEquals(expected, actual)
    }
}