package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.ProfilesRepository

/**
 * Юзкейс, который загружает изображение профиля на сервер
 * и возвращает ссылку на загруженное изображение профиля.
 */
class UploadAvatar(private val profilesRepository: ProfilesRepository) {
    suspend operator fun invoke(
        path: String,
        content: ByteArray,
    ) = profilesRepository.uploadAvatar(path, content)
}