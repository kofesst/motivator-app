package me.kofesst.android.motivatorapp

import me.kofesst.android.motivatorapp.presentation.screen.Screen
import me.kofesst.android.motivatorapp.presentation.screen.ScreenConstants
import me.kofesst.android.motivatorapp.presentation.screen.toRouteName
import me.kofesst.android.motivatorapp.presentation.screen.withArgs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

/**
 * Unit тесты для проверки корректной работы
 * функций внутри класса [Screen].
 */
class ScreensTest {
    companion object {
        /**
         * Параметры для теста [Screen.equals].
         */
        @JvmStatic
        fun equalsTestParameters(): Stream<Arguments> = Stream.of(
            Arguments.of(
                Screen.ProfileOverview,
                Screen.ProfileOverview,
                null,
                true
            ),
            Arguments.of(
                Screen.PostOverview,
                Screen.PostOverview,
                ScreenConstants.ID_ARG_NAME to "test",
                true
            ),
            Arguments.of(
                Screen.ProfileOverview,
                Screen.Feed,
                ScreenConstants.ID_ARG_NAME to "test",
                false
            ),
        )

        @JvmStatic
        fun toRouteNameTestParameters(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "route",
                "route"
            ),
            Arguments.of(
                "route?filter=true",
                "route"
            ),
            Arguments.of(
                "route/profileId?filter=true",
                "route"
            ),
            Arguments.of(
                "route/profileId?filter=true",
                "route"
            ),
        )
    }

    /**
     * Параметризованный тест на проверку
     * корректности функции преобразования
     * строки в [Screen.routeName].
     */
    @ParameterizedTest
    @MethodSource("toRouteNameTestParameters")
    fun `toRouteName() with string works as expected`(
        route: String,
        expected: String,
    ) {
        val actual = route.toRouteName()
        assertEquals(expected, actual)
    }

    /**
     * Параметризованный тест на проверку
     * корректности функции сравнения объекта
     * [Screen] и [Screen.routeName].
     */
    @ParameterizedTest
    @MethodSource("equalsTestParameters")
    fun `equals() with string works as expected`(
        screen: Screen<*>,
        comparingScreen: Screen<*>,
        comparingArgs: Pair<String, Any>?,
        expected: Boolean,
    ) {
        val route = if (comparingArgs != null) {
            comparingScreen.withArgs(comparingArgs)
        } else {
            comparingScreen.routeName
        }
        val actual = screen.equals(route)
        assertEquals(expected, actual)
    }
}