package me.kofesst.android.motivatorapp.presentation.profile.edit

sealed class ProfileFormAction {
    data class FirstNameChanged(val firstName: String) : ProfileFormAction()
    data class LastNameChanged(val lastName: String) : ProfileFormAction()
    data class AvatarChanged(val fileContent: ByteArray) : ProfileFormAction()

    object Submit : ProfileFormAction()
}
