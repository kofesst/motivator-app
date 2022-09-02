package me.kofesst.android.motivatorapp.presentation.auth

import me.kofesst.android.motivatorapp.presentation.utils.UiText

data class AuthFormState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val firstName: String = "",
    val firstNameError: UiText? = null,
    val lastName: String = "",
    val lastNameError: UiText? = null,
    val rememberSession: Boolean = true,
)
