package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.AuthRepository

/**
 * Юзкейс, возвращающий ID авторизованного
 * в приложении пользователя.
 */
class GetLoggedProfileId(
    private val authRepository: AuthRepository,
) {
    operator fun invoke() = authRepository.getLoggedProfileId()
}