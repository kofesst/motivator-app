package me.kofesst.android.motivatorapp.domain.models

import me.kofesst.android.motivatorapp.domain.models.MotivationPost.Difficulty.*
import me.kofesst.android.motivatorapp.domain.models.constants.AppConstants
import java.util.*

/**
 * Модель мотивационной записи пользователя.
 *
 * [authorId] - идентификатор профиля автора.
 *
 * [title] - наименование записи.
 *
 * [description] - описание записи.
 *
 * [difficulty] - сложность записи.
 *
 * [timestamp] - дата создания записи.
 *
 * [deadline] - "срок действия" записи.
 *
 * [completed] - выполнена ли запись автором.
 *
 * [id] - уникальный идентификатор записи.
 */
data class MotivationPost(
    val authorId: String = "",
    val title: String,
    val description: String,
    val difficulty: Difficulty,
    val timestamp: Date = Date(0),
    val deadline: Date = Date(0),
    var completed: Boolean = false,
    val id: String = "${timestamp.time}_${UUID.randomUUID()}",
) {
    /**
     * Истек ли "срок действия" записи.
     */
    val expired: Boolean
        get() = !completed && Date().time > deadline.time

    /**
     * Перечисление типов сложности записи.
     *
     * [Easy] - самая низкая сложность.
     *
     * [Normal] - обычная сложность.
     *
     * [Hard] - самая высокая сложность.
     *
     * [rating] - количество рейтинга, которое
     * или отнимется у пользователя (при истечении срока записи),
     * или прибавится к его рейтингу (при успешном выполнении).
     */
    enum class Difficulty(val rating: Int) {
        Easy(AppConstants.Posts.Difficulty.EASY_RATING),
        Normal(AppConstants.Posts.Difficulty.NORMAL_RATING),
        Hard(AppConstants.Posts.Difficulty.HARD_RATING)
    }
}
