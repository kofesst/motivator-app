package me.kofesst.android.motivatorapp.domain.repository

import me.kofesst.android.motivatorapp.domain.models.results.AuthResult

/**
 * Репозиторий, отвечающий за функции авторизации.
 */
interface AuthRepository {
    /**
     * Возвращает ID авторизованного в системе пользователя.
     * Если пользователь не авторизован, возвращает null.
     */
    fun getLoggedProfileId(): String?

    /**
     * Выполняет функцию регистрации и возвращает ее результат [AuthResult].
     *
     * [email] - Email пользователя.
     *
     * [password] - пароль пользователя.
     *
     * [firstName] - имя пользователя.
     *
     * [lastName] - фамилия пользователя.
     *
     * [rememberSession] - нужно ли сохранять данные для входа.
     */
    suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        rememberSession: Boolean,
    ): AuthResult

    /**
     * Выполняет функцию авторизации и возвращает ее результат [AuthResult].
     *
     * [email] - Email пользователя.
     *
     * [password] - пароль пользователя.
     *
     * [rememberSession] - нужно ли сохранять данные для входа.
     */
    suspend fun signIn(
        email: String,
        password: String,
        rememberSession: Boolean,
    ): AuthResult

    suspend fun restoreSession(): Pair<String, String>?
    suspend fun clearSession()
}