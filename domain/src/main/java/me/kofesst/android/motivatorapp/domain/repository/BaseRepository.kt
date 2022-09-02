package me.kofesst.android.motivatorapp.domain.repository

import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.UserProfile

/**
 * Базовый репозиторий, выполняющий основные функции
 * получения, изменения, удаления информации.
 */
interface BaseRepository {
    /**
     * Возвращает модель профиля пользователя [UserProfile].
     * Если профиля с таким [id] нет, возвращает null.
     *
     * [id] - ID профиля.
     */
    suspend fun getProfile(id: String): UserProfile?

    /**
     * Обновляет модель профиля пользователя [UserProfile].
     *
     * [profile] - модель профиля, которую нужно обновить.
     */
    suspend fun updateProfile(profile: UserProfile)

    /**
     * Удаляет профиль авторизованного в приложении
     * пользователя.
     */
    suspend fun deleteCurrentProfile()

    /**
     * Создает новую запись [post].
     *
     * [post] - модель записи, которую нужно добавить в базу.
     */
    suspend fun createPost(post: MotivationPost)

    /**
     * Возвращает модель записи [MotivationPost].
     * Если записи с таким [id] нет, возвращает null.
     *
     * [id] - ID записи.
     */
    suspend fun getPost(id: String): MotivationPost?

    /**
     * Возвращает список моделей записей [MotivationPost], фильтруя их
     * при помощи [selector].
     *
     * [selector] - фильтр записей.
     */
    suspend fun getPosts(selector: (MotivationPost) -> Boolean = { true }): List<MotivationPost>

    /**
     * Обновляет модель записи [MotivationPost].
     *
     * [post] - модель записи, которую нужно обновить.
     */
    suspend fun updatePost(post: MotivationPost)
}