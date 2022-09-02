package me.kofesst.android.motivatorapp.presentation.auth

enum class AuthScreenState {
    SignUp, SignIn;

    fun opposite() = when (this) {
        SignUp -> SignIn
        SignIn -> SignUp
    }
}