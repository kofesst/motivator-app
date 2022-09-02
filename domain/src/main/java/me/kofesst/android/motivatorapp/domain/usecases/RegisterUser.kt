package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.AuthRepository

/**
 * Юзкейс, выполняющий функцию регистрации пользователя и
 * возвращающий результат.
 */
class RegisterUser(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        rememberSession: Boolean,
    ) = authRepository.register(email, password, firstName, lastName, rememberSession)
}