package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Юзкейс, удаляющий профиль авторизованного
 * в приложении пользователя.
 */
class DeleteCurrentProfile(
    private val baseRepository: BaseRepository,
) {
    suspend operator fun invoke() =
        baseRepository.deleteCurrentProfile()
}