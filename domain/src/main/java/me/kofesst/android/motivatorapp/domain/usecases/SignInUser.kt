package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.AuthRepository

/**
 * Юзкейс, выполняющий функцию авторизации пользователя
 * и возвращающий результат.
 */
class SignInUser(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        rememberSession: Boolean,
    ) =
        authRepository.signIn(email, password, rememberSession)
}