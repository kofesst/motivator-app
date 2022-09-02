package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Юзкейс, создающий новую запись.
 */
class CreatePost(
    private val baseRepository: BaseRepository,
) {
    suspend operator fun invoke(post: MotivationPost) =
        baseRepository.createPost(post)
}