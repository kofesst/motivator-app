package me.kofesst.android.motivatorapp.domain.repository

import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor

/**
 * Репозиторий, отвечающий за объединение моделей
 * профиля и записи в модель [MotivationPostWithAuthor].
 */
interface PostsRepository {
    /**
     * Возвращает список всех записей [MotivationPostWithAuthor], созданных
     * авторизованным в приложении пользователем.
     */
    suspend fun getPostsWithAuthor(): List<MotivationPostWithAuthor>

    /**
     * Возвращает модель записи с автором [MotivationPostWithAuthor].
     *
     * [id] - ID записи.
     */
    suspend fun getPostWithAuthor(id: String): MotivationPostWithAuthor?
}