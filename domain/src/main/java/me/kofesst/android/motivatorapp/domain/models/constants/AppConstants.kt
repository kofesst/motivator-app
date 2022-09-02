package me.kofesst.android.motivatorapp.domain.models.constants

/**
 * Неизменные значения и ограничения для всех моделей приложения.
 */
sealed class AppConstants private constructor() {
    /**
     * Ограничения для профиля пользователя.
     */
    sealed class Profile private constructor() : AppConstants() {
        companion object {
            val NAME_LENGTH_RANGE = 2..15
            val PASSWORD_LENGTH_RANGE = 6..20
        }
    }

    /**
     * Ограничения для записи.
     */
    sealed class Posts private constructor() : AppConstants() {
        companion object {
            val TITLE_LENGTH_RANGE = 5..40
            val DESCRIPTION_LENGTH_RANGE = 5..150
        }

        /**
         * Значения для сложности записи.
         */
        class Difficulty private constructor() : Posts() {
            companion object {
                const val EASY_RATING = 1
                const val NORMAL_RATING = 3
                const val HARD_RATING = 9
            }
        }
    }
}