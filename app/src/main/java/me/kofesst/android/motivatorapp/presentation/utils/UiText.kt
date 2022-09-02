package me.kofesst.android.motivatorapp.presentation.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import me.kofesst.android.motivatorapp.R
import me.kofesst.android.motivatorapp.domain.models.MotivationPost

sealed class UiText {
    var defaultFormats = listOf<Any>()

    @Composable
    protected abstract fun asString(vararg formats: Any): String

    protected abstract fun asString(context: Context, vararg formats: Any): String

    @Composable
    operator fun invoke(vararg formats: Any) =
        asString(formats = formats)

    operator fun invoke(context: Context, vararg formats: Any) =
        asString(context = context, formats = formats)

    data class Static(val text: String) : UiText() {
        @Composable
        override fun asString(vararg formats: Any): String =
            text.format(*(defaultFormats + formats).toTypedArray())

        override fun asString(context: Context, vararg formats: Any): String =
            text.format(*(defaultFormats + formats).toTypedArray())
    }

    data class Resource(@StringRes val resId: Int) : UiText() {
        @Composable
        override fun asString(vararg formats: Any): String =
            stringResource(id = resId, *(defaultFormats + formats).toTypedArray())

        override fun asString(context: Context, vararg formats: Any): String =
            context.resources.getString(resId, *(defaultFormats + formats).toTypedArray())
    }
}

sealed class AppText {
    object Error : AppText() {
        val emptyFieldError = UiText.Resource(R.string.error__empty_field)
        val smallLengthError = UiText.Resource(R.string.error__small_length)
        val longLengthError = UiText.Resource(R.string.error__long_length)
        val invalidEmailError = UiText.Resource(R.string.error__invalid_email)
        val passwordContainsSpaceError = UiText.Resource(R.string.error__password_contains_space)
        val passwordNeedDigitError = UiText.Resource(R.string.error__password_need_digit)
        val passwordNeedLetterError = UiText.Resource(R.string.error__password_need_letter)
        val emailDoesNotExistError = UiText.Resource(R.string.error__email_does_not_exist)
        val emailAlreadyExistsError = UiText.Resource(R.string.error__email_already_exists)
        val incorrectPasswordError = UiText.Resource(R.string.error__incorrect_password)
    }

    object Title : AppText() {
        val feed = UiText.Resource(R.string.title__feed)
        val profileOverview = UiText.Resource(R.string.title__profile_overview)
        val auth = UiText.Resource(R.string.title__auth)
        val editProfile = UiText.Resource(R.string.title__edit_profile)
        val postOverview = UiText.Resource(R.string.title__post_overview)
        val createPost = UiText.Resource(R.string.title__create_post)
    }

    object Action : AppText() {
        val signIn = UiText.Resource(R.string.action__sign_in)
        val signUp = UiText.Resource(R.string.action__sign_up)
        val createPost = UiText.Resource(R.string.action__create_post)
        val editProfile = UiText.Resource(R.string.action__edit_profile)
        val saveProfile = UiText.Resource(R.string.action__save_profile)
        val uploadAvatar = UiText.Resource(R.string.action__upload_avatar)
        val removeAvatar = UiText.Resource(R.string.action__remove_avatar)
        val completePost = UiText.Resource(R.string.action__complete_post)
        val rememberSession = UiText.Resource(R.string.action__remember_session)
        val clearSession = UiText.Resource(R.string.action__clear_session)
    }

    object Label : AppText() {
        val email = UiText.Resource(R.string.label__email)
        val password = UiText.Resource(R.string.label__password)
        val firstName = UiText.Resource(R.string.label__first_name)
        val lastName = UiText.Resource(R.string.label__last_name)
        val postTitle = UiText.Resource(R.string.label__post_title)
        val postDescription = UiText.Resource(R.string.label__post_description)
        val profileAvatar = UiText.Resource(R.string.label__profile_avatar)
    }

    object Snackbar : AppText() {
        val cannotGetPostId = UiText.Resource(R.string.snackbar__cannot_get_post_id)
        val needStoragePermission = UiText.Resource(R.string.snackbar__need_storage_permission)
        val postCompleted = UiText.Resource(R.string.snackbar__post_completed)
    }

    object Case : AppText() {
        val postsFirstCase = UiText.Resource(R.string.case__posts_first)
        val postsSecondCase = UiText.Resource(R.string.case__posts_second)
        val postsThirdCase = UiText.Resource(R.string.case__posts_third)

        val ratingFirstCase = UiText.Resource(R.string.case__rating_first)
        val ratingSecondCase = UiText.Resource(R.string.case__rating_second)
        val ratingThirdCase = UiText.Resource(R.string.case__rating_third)
    }

    object Difficulty : AppText() {
        val easy = UiText.Resource(R.string.difficulty__easy)
        val normal = UiText.Resource(R.string.difficulty__normal)
        val hard = UiText.Resource(R.string.difficulty__hard)
    }

    object PostStatus : AppText() {
        val waiting = UiText.Resource(R.string.post_status__waiting)
        val completed = UiText.Resource(R.string.post_status_completed)
        val expired = UiText.Resource(R.string.post_status_expired)
    }

    companion object {
        val authWelcomeText = UiText.Resource(R.string.auth_welcome)
        val authSubtitleText = UiText.Resource(R.string.auth_subtitle)
        val profilePosts = UiText.Resource(R.string.profile_posts)
        val alreadyHasProfile = UiText.Resource(R.string.already_has_profile)
        val hasNotProfileText = UiText.Resource(R.string.has_not_profile)
        val thereIsEmptyText = UiText.Resource(R.string.there_is_empty)
        val checkingForSession = UiText.Resource(R.string.checking_for_session)
    }
}

val MotivationPost.Difficulty.title: UiText
    get() = when (this) {
        MotivationPost.Difficulty.Easy -> AppText.Difficulty.easy
        MotivationPost.Difficulty.Normal -> AppText.Difficulty.normal
        MotivationPost.Difficulty.Hard -> AppText.Difficulty.hard
    }