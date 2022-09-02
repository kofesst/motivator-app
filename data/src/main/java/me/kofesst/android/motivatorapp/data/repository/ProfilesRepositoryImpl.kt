package me.kofesst.android.motivatorapp.data.repository

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import me.kofesst.android.motivatorapp.domain.models.UserProfileWithPosts
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository
import me.kofesst.android.motivatorapp.domain.repository.ProfilesRepository

/**
 * Реализация интерфейса репозитория [ProfilesRepository].
 */
class ProfilesRepositoryImpl(
    private val baseRepository: BaseRepository,
) : ProfilesRepository {
    override suspend fun getProfileWithPosts(id: String): UserProfileWithPosts? {
        val profile = baseRepository.getProfile(id) ?: return null
        val posts = baseRepository.getPosts { post ->
            post.authorId == id
        }

        return UserProfileWithPosts(
            profile = profile,
            posts = posts
        )
    }

    override suspend fun uploadAvatar(
        path: String,
        fileContent: ByteArray,
    ): String {
        val storage = Firebase.storage
        val avatarReference = storage.getReference(path)
        avatarReference.putBytes(fileContent).await()
        return avatarReference.downloadUrl.await().toString()
    }
}