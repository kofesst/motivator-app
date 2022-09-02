package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.models.UserProfile
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Юзкейс, который обновляет модель профиля в базе.
 */
class UpdateProfile(
    private val baseRepository: BaseRepository,
) {
    suspend operator fun invoke(profile: UserProfile) =
        baseRepository.updateProfile(profile)
}