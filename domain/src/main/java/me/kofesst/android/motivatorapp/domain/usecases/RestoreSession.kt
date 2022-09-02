package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.AuthRepository

class RestoreSession(private val repository: AuthRepository) {
    suspend operator fun invoke() =
        repository.restoreSession()
}