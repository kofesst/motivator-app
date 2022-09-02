package me.kofesst.android.motivatorapp

import me.kofesst.android.motivatorapp.presentation.utils.addDays
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.*
import java.util.stream.Stream

/**
 * Unit тесты, проверяющие на корректность
 * функций, работающих с датой.
 */
class DateUtilsTest {
    companion object {
        /**
         * Тестовая дата.
         */
        private val testDate = Date(1000 * 60 * 60 * 24 * 2)

        /**
         * Параметры для теста.
         */
        @JvmStatic
        fun addDaysParameters(): Stream<Arguments> = Stream.of(
            Arguments.of(0, 3),
            Arguments.of(1, 4),
            Arguments.of(2, 5),
            Arguments.of(3, 6),
            Arguments.of(4, 7),
            Arguments.of(5, 8),
            Arguments.of(6, 9),
            Arguments.of(7, 10),
            Arguments.of(28, 31),
            Arguments.of(29, 1),
            Arguments.of(-1, 2),
            Arguments.of(-2, 1),
        )
    }

    /**
     * Unit тест для функции [Date.addDays].
     */
    @ParameterizedTest
    @MethodSource("addDaysParameters")
    fun `AddDays() works as expected`(days: Int, expectedDays: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = testDate.addDays(days)
        val actualDays = calendar.get(Calendar.DAY_OF_MONTH)
        assertEquals(expectedDays, actualDays)
    }
}