package me.kofesst.android.motivatorapp.presentation.profile.edit

import me.kofesst.android.motivatorapp.presentation.utils.UiText

data class ProfileFormState(
    val firstName: String = "",
    val firstNameError: UiText? = null,
    val lastName: String = "",
    val lastNameError: UiText? = null,
    val avatarContent: ByteArray = byteArrayOf(),
)
