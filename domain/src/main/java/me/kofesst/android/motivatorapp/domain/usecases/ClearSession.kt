package me.kofesst.android.motivatorapp.domain.usecases

import me.kofesst.android.motivatorapp.domain.repository.AuthRepository

class ClearSession(private val repository: AuthRepository) {
    suspend operator fun invoke() =
        repository.clearSession()
}