package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.PostsRepository

/**
 * Юзкейс, возвращающий модель записи, включающую
 * модель профиля автора.
 */
class GetPostWithAuthor(
    private val postsRepository: PostsRepository,
) {
    suspend operator fun invoke(id: String) =
        postsRepository.getPostWithAuthor(id)
}