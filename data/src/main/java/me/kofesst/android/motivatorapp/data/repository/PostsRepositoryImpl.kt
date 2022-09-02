package me.kofesst.android.motivatorapp.data.repository

import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository
import me.kofesst.android.motivatorapp.domain.repository.PostsRepository

/**
 * Реализация интерфейса репозитория [PostsRepository].
 */
class PostsRepositoryImpl(
    private val baseRepository: BaseRepository,
) : PostsRepository {
    override suspend fun getPostsWithAuthor(): List<MotivationPostWithAuthor> {
        val posts = baseRepository.getPosts()
        return posts.map { post ->
            val authorId = post.authorId
            val author = baseRepository.getProfile(authorId) ?: return@map null
            MotivationPostWithAuthor(
                post = post,
                author = author
            )
        }.filterNotNull()
    }

    override suspend fun getPostWithAuthor(id: String): MotivationPostWithAuthor? {
        val post = baseRepository.getPost(id) ?: return null
        val authorId = post.authorId
        val author = baseRepository.getProfile(authorId) ?: return null

        return MotivationPostWithAuthor(
            post = post,
            author = author
        )
    }
}