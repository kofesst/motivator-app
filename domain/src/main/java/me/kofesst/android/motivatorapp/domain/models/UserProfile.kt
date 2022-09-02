package me.kofesst.android.motivatorapp.domain.models

import me.kofesst.android.motivatorapp.domain.BuildConfig

/**
 * Модель профиля пользователя.
 *
 * [id] - уникальный идентификатор профиля.
 *
 * [firstName] - имя пользователя.
 *
 * [lastName] - фамилия пользователя.
 *
 * [avatarUrl] - ссылка на изображение профиля.
 */
data class UserProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
) {
    /**
     * Ссылка на изображение профиля, которая
     * будет использоваться в приложении.
     *
     * Если [avatarUrl] пустая (у пользователя нет
     * своего изображения профиля), то будет использоваться
     * изображение профиля по умолчанию.
     */
    val appAvatarUrl: String
        get() = avatarUrl.ifBlank {
            BuildConfig.DEFAULT_AVATAR_URL
        }

    /**
     * Полное имя пользователя в формате
     * [firstName] + " " + [lastName].
     */
    val fullName: String
        get() = "$firstName $lastName"
}