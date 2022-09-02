package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.PostsRepository

/**
 * Юзкейс, возвращающий список моделей записей,
 * включающий модель профиля автора.
 */
class GetPostsWithAuthor(
    private val postsRepository: PostsRepository,
) {
    suspend operator fun invoke() =
        postsRepository.getPostsWithAuthor()
}