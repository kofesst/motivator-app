package me.kofesst.android.motivatorapp.domain.models.results

/**
 * Результат авторизации или регистрации.
 */
sealed class AuthResult {
    /**
     * Результат успешной авторизации или регистрации.
     */
    sealed class Success : AuthResult() {
        /**
         * Результат успешной регистрации.
         */
        object Registered : Success()

        /**
         * Результат успешной авторизации.
         */
        object SignedIn : Success()
    }

    /**
     * Результат неудачной авторизации или регистрации.
     */
    sealed class Failed : AuthResult() {
        /**
         * Результат того, что email, который указывает
         * пользователь при регистрации, уже занят.
         */
        object EmailAlreadyExists : Failed()

        /**
         * Результат того, что email, который указывает
         * пользователь при авторизации, не существует.
         */
        object EmailDoesNotExist : Failed()

        /**
         * Результат того, что пароль, который указывает
         * пользователь при авторизации, не подходит.
         */
        object IncorrectPassword : Failed()

        /**
         * Результат того, что во время отправки данных на сервер
         * произошла неожиданная ошибка.
         */
        data class Unexpected(val exception: Exception) : Failed()
    }
}
