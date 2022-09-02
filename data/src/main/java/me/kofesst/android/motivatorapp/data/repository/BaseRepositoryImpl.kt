package me.kofesst.android.motivatorapp.data.repository

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import me.kofesst.android.motivatorapp.data.BuildConfig
import me.kofesst.android.motivatorapp.data.models.MotivationPostDto
import me.kofesst.android.motivatorapp.data.models.UserProfileDto
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.UserProfile
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Реализация интерфейса репозитория [BaseRepository].
 */
class BaseRepositoryImpl : BaseRepository {
    private val profilesReference: DatabaseReference
        get() = FirebaseDatabase.getInstance(BuildConfig.DB_URL).getReference("profiles")

    private val postsReference: DatabaseReference
        get() = FirebaseDatabase.getInstance(BuildConfig.DB_URL).getReference("posts")

    override suspend fun getProfile(id: String): UserProfile? {
        val profileSnapshot = profilesReference.child(id).get().await()
        val profileDto = profileSnapshot.getValue<UserProfileDto>()
        return profileDto?.toDomain()
    }

    override suspend fun updateProfile(profile: UserProfile) {
        val profileReference = profilesReference.child(profile.id)
        val profileDto = UserProfileDto.fromDomain(profile)
        profileReference.setValue(profileDto).await()
    }

    override suspend fun deleteCurrentProfile() {
        val firebaseUser = Firebase.auth.currentUser ?: return
        val currentProfileId = firebaseUser.uid
        val profileReference = profilesReference.child(currentProfileId)
        profileReference.setValue(null).await()
    }

    override suspend fun createPost(post: MotivationPost) {
        val firebaseUser = Firebase.auth.currentUser ?: return
        val currentProfileId = firebaseUser.uid

        this.updatePost(
            post.copy(
                authorId = currentProfileId
            )
        )
    }

    override suspend fun getPost(id: String): MotivationPost? {
        val postSnapshot = postsReference.child(id).get().await()
        val postDto = postSnapshot.getValue<MotivationPostDto>()
        return postDto?.toDomain()
    }

    override suspend fun getPosts(selector: (MotivationPost) -> Boolean): List<MotivationPost> {
        val postsSnapshot = postsReference.get().await()
        return postsSnapshot.children.mapNotNull { snapshot ->
            val postDto = snapshot.getValue<MotivationPostDto>()
            postDto?.toDomain()
        }
    }

    override suspend fun updatePost(post: MotivationPost) {
        val postReference = postsReference.child(post.id)
        val postDto = MotivationPostDto.fromDomain(post)
        postReference.setValue(postDto).await()
    }
}