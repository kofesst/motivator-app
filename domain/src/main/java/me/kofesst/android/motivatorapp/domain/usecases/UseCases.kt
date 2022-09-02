package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.AuthRepository
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository
import me.kofesst.android.motivatorapp.domain.repository.PostsRepository
import me.kofesst.android.motivatorapp.domain.repository.ProfilesRepository
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidateForEmail
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidateForEmptyField
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidateForLength
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidateForPassword

/**
 * Класс, представляющий все юзкейсы.
 */
class UseCases(
    baseRepository: BaseRepository,
    authRepository: AuthRepository,
    profilesRepository: ProfilesRepository,
    postsRepository: PostsRepository,

    val getLoggedProfileId: GetLoggedProfileId = GetLoggedProfileId(authRepository),
    val registerUser: RegisterUser = RegisterUser(authRepository),
    val signInUser: SignInUser = SignInUser(authRepository),
    val clearSession: ClearSession = ClearSession(authRepository),
    val restoreSession: RestoreSession = RestoreSession(authRepository),

    val getProfile: GetProfile = GetProfile(baseRepository),
    val getProfileWithPosts: GetProfileWithPosts = GetProfileWithPosts(profilesRepository),
    val updateProfile: UpdateProfile = UpdateProfile(baseRepository),
    val deleteCurrentProfile: DeleteCurrentProfile = DeleteCurrentProfile(baseRepository),
    val uploadAvatar: UploadAvatar = UploadAvatar(profilesRepository),

    val createPost: CreatePost = CreatePost(baseRepository),
    val getPostsWithAuthor: GetPostsWithAuthor = GetPostsWithAuthor(postsRepository),
    val getPostWithAuthor: GetPostWithAuthor = GetPostWithAuthor(postsRepository),
    val updatePost: UpdatePost = UpdatePost(baseRepository),

    val validateForEmptyField: ValidateForEmptyField = ValidateForEmptyField(),
    val validateForEmail: ValidateForEmail = ValidateForEmail(),
    val validateForPassword: ValidateForPassword = ValidateForPassword(),
    val validateForLength: ValidateForLength = ValidateForLength(),
)
