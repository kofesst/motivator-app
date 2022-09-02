package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Юзкейс, возвращающий модель профиля пользователя.
 */
class GetProfile(
    private val baseRepository: BaseRepository,
) {
    suspend operator fun invoke(id: String) =
        baseRepository.getProfile(id)
}