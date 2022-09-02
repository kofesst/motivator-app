package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.ProfilesRepository

/**
 * Юзкейс, возвращающий модель пользователя,
 * включающую список моделей записей, созданных
 * пользователем.
 */
class GetProfileWithPosts(
    private val profilesRepository: ProfilesRepository,
) {
    suspend operator fun invoke(id: String) =
        profilesRepository.getProfileWithPosts(id)
}