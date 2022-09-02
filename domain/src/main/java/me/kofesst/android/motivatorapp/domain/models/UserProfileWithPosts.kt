package me.kofesst.android.motivatorapp.domain.models

/**
 * Модель профиля пользователя, включающая
 * в себя список моделей созданных автором
 * мотивационных записей.
 *
 * [profile] - модель профиля.
 *
 * [posts] - список моделей созданных автором записей.
 */
data class UserProfileWithPosts(
    val profile: UserProfile,
    val posts: List<MotivationPost>,
) {
    /**
     * Сумма рейтинга, считающаяся следующим образом:
     *
     * - Если запись не выполнена, то она не имеет веса.
     *
     * - Если запись выполнена, то ее вес равен рейтингу ее
     * сложности.
     *
     * - Если срок действия записи истек, то ее вес равен
     * отрицительному рейтингу ее сложности.
     */
    val userRating: Int
        get() = posts.sumOf { post ->
            if (post.completed) {
                post.difficulty.rating
            } else if (post.expired) {
                -post.difficulty.rating
            } else {
                0
            }
        }
}