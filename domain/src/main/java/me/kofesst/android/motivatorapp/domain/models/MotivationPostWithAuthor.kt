package me.kofesst.android.motivatorapp.domain.models

/**
 * Модель мотивационной записи, включающая
 * в себя модель профиля автора.
 *
 * [post] - модель записи.
 *
 * [author] - модель профиля автора.
 */
data class MotivationPostWithAuthor(
    val post: MotivationPost,
    val author: UserProfile,
)
