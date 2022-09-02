package me.kofesst.android.motivatorapp.data.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import me.kofesst.android.motivatorapp.data.models.UserProfileDto
import me.kofesst.android.motivatorapp.domain.models.results.AuthResult
import me.kofesst.android.motivatorapp.domain.repository.AuthRepository
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository

/**
 * Реализация интерфейса репозитория [AuthRepository].
 */
class AuthRepositoryImpl(
    private val baseRepository: BaseRepository,
    private val dataStore: DataStore<Preferences>,
) : AuthRepository {
    companion object {
        private val EMAIL_SESSION_KEY = stringPreferencesKey("session_email")
        private val PASSWORD_SESSION_KEY = stringPreferencesKey("session_password")
    }

    private val auth get() = Firebase.auth

    override fun getLoggedProfileId(): String? {
        return auth.uid
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        rememberSession: Boolean,
    ): AuthResult = handleAuth {
        val firebaseResult = auth.createUserWithEmailAndPassword(email, password).await()
        val firebaseUser = firebaseResult.user
            ?: throw NullPointerException("Firebase user is null")

        val userProfileDto = UserProfileDto(
            id = firebaseUser.uid,
            firstName = firstName,
            lastName = lastName
        )
        baseRepository.updateProfile(userProfileDto.toDomain())

        if (rememberSession) {
            dataStore.edit { preferences ->
                preferences[EMAIL_SESSION_KEY] = email
                preferences[PASSWORD_SESSION_KEY] = password
            }
        }

        AuthResult.Success.Registered
    }

    override suspend fun signIn(
        email: String,
        password: String,
        rememberSession: Boolean,
    ): AuthResult = handleAuth {
        val firebaseResult = auth.signInWithEmailAndPassword(email, password).await()
        val firebaseUser = firebaseResult.user
            ?: throw NullPointerException("Firebase user is null")

        // Проверка на наличие профиля пользователя
        baseRepository.getProfile(firebaseUser.uid)
            ?: throw NullPointerException("User profile is null")

        if (rememberSession) {
            dataStore.edit { preferences ->
                preferences[EMAIL_SESSION_KEY] = email
                preferences[PASSWORD_SESSION_KEY] = password
            }
        }

        AuthResult.Success.SignedIn
    }

    override suspend fun restoreSession(): Pair<String, String>? {
        return dataStore.data.map { preferences ->
            val email = preferences[EMAIL_SESSION_KEY] ?: return@map null
            val password = preferences[PASSWORD_SESSION_KEY] ?: return@map null

            email to password
        }.firstOrNull()
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(EMAIL_SESSION_KEY)
            preferences.remove(PASSWORD_SESSION_KEY)
        }
    }

    private suspend fun handleAuth(block: suspend () -> AuthResult) = try {
        block()
    } catch (exception: Exception) {
        Log.d("AAA", exception.stackTraceToString())
        when (exception) {
            is FirebaseAuthInvalidUserException -> AuthResult.Failed.EmailDoesNotExist
            is FirebaseAuthInvalidCredentialsException -> AuthResult.Failed.IncorrectPassword
            is FirebaseAuthUserCollisionException -> AuthResult.Failed.EmailAlreadyExists
            else -> AuthResult.Failed.Unexpected(exception)
        }
    }
}