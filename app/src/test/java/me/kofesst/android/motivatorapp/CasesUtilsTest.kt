package me.kofesst.android.motivatorapp

import me.kofesst.android.motivatorapp.presentation.utils.WordCases
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * Unit тесты, проверяющие на корректность
 * выбора склонения слова в зависимости от количества
 * с помощью функции [WordCases.getCase].
 */
class CasesUtilsTest {
    companion object {
        /**
         * Тестовая модель склонений слова.
         */
        private val testCases = WordCases(
            firstCase = "firstCase",
            secondCase = "secondCase",
            thirdCase = "thirdCase"
        )

        /**
         * Параметры для теста.
         */
        @JvmStatic
        fun testParameters(): Stream<Arguments> = Stream.of(
            Arguments.of(1, "firstCase"),
            Arguments.of(21, "firstCase"),
            Arguments.of(101, "firstCase"),

            Arguments.of(2, "secondCase"),
            Arguments.of(3, "secondCase"),
            Arguments.of(4, "secondCase"),
            Arguments.of(22, "secondCase"),
            Arguments.of(103, "secondCase"),

            Arguments.of(5, "thirdCase"),
            Arguments.of(6, "thirdCase"),
            Arguments.of(11, "thirdCase"),
            Arguments.of(15, "thirdCase"),
            Arguments.of(111, "thirdCase"),
            Arguments.of(118, "thirdCase"),
        )
    }

    /**
     * Unit тест для функции [WordCases.getCase].
     */
    @ParameterizedTest
    @MethodSource("testParameters")
    fun `GetCase() works as expected`(amount: Int, expectedCase: String) {
        val actualCase = testCases.getCase(amount)
        assertEquals(expectedCase, actualCase)
    }
}