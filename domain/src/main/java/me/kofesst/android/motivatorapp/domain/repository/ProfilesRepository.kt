package me.kofesst.android.motivatorapp.domain.repository

import me.kofesst.android.motivatorapp.domain.models.UserProfileWithPosts

/**
 * Репозиторий, отвечающий за действия пользователя.
 */
interface ProfilesRepository {
    /**
     * Возвращает модель профиля с созданными им записями
     * [UserProfileWithPosts].
     * Если модели профиля с таким [id] нет, возвращает null.
     *
     * [id] - ID профиля.
     */
    suspend fun getProfileWithPosts(id: String): UserProfileWithPosts?

    /**
     * Загружает изображение профиля на сервер и возвращает
     * ссылку на загруженное изображение.
     *
     * [path] - путь, по которому надо сохранить изображение.
     *
     * [fileContent] - содержимое файла, которое надо сохранить.
     */
    suspend fun uploadAvatar(path: String, fileContent: ByteArray): String
}