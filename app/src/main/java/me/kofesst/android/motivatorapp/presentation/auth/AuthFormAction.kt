package me.kofesst.android.motivatorapp.presentation.auth

sealed class AuthFormAction {
    data class EmailChanged(val email: String) : AuthFormAction()
    data class PasswordChanged(val password: String) : AuthFormAction()
    data class FirstNameChanged(val firstName: String) : AuthFormAction()
    data class LastNameChanged(val lastName: String) : AuthFormAction()
    data class RememberSessionChanged(val value: Boolean) : AuthFormAction()
    object Submit : AuthFormAction()
}
