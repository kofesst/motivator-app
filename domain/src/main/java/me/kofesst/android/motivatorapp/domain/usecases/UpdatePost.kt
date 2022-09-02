package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Юзкейс, который обновляет модель записи в базе.
 */
class UpdatePost(private val repository: BaseRepository) {
    suspend operator fun invoke(post: MotivationPost) =
        repository.updatePost(post)
}