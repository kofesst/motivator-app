package me.kofesst.android.motivatorapp.presentation.profile.edit

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.kofesst.android.motivatorapp.domain.models.UserProfile
import me.kofesst.android.motivatorapp.domain.models.constants.AppConstants
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidationResult
import me.kofesst.android.motivatorapp.presentation.utils.FormViewModel
import me.kofesst.android.motivatorapp.presentation.utils.LoadingState
import me.kofesst.android.motivatorapp.presentation.utils.errorMessage
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val useCases: UseCases,
) : FormViewModel<ProfileFormState, ProfileFormResult, ProfileFormAction>(
    initialFormState = ProfileFormState()
) {
    private val _profileState = mutableStateOf<LoadingState<UserProfile>>(LoadingState.Idle())
    val profileState: State<LoadingState<UserProfile>> = _profileState

    fun loadProfile(id: String) {
        viewModelScope.launch {
            _profileState.value = LoadingState.Loading()
            _profileState.value = useCases.getProfile(id).run {
                when (this) {
                    null -> LoadingState.Failed(
                        exception = NullPointerException("Cannot load profile")
                    )
                    else -> {
                        formState = formState.copy(
                            firstName = this.firstName,
                            lastName = this.lastName,
                            avatarContent = byteArrayOf()
                        )

                        LoadingState.Loaded(
                            value = this
                        )
                    }
                }
            }
        }
    }

    override fun onFormAction(action: ProfileFormAction) {
        formState = when (action) {
            is ProfileFormAction.FirstNameChanged -> {
                formState.copy(firstName = action.firstName)
            }
            is ProfileFormAction.LastNameChanged -> {
                formState.copy(lastName = action.lastName)
            }
            is ProfileFormAction.AvatarChanged -> {
                formState.copy(avatarContent = action.fileContent)
            }
            ProfileFormAction.Submit -> {
                onSubmit()
                return
            }
        }
    }

    override fun validateFields(): List<ValidationResult> {
        val (firstNameResult, lastNameResult) = useCases.validateForLength(
            value = formState.firstName,
            lengthRange = AppConstants.Profile.NAME_LENGTH_RANGE
        ) to useCases.validateForLength(
            value = formState.firstName,
            lengthRange = AppConstants.Profile.NAME_LENGTH_RANGE
        )
        return listOf(firstNameResult, lastNameResult)
    }

    override fun validationResultsToFormState(results: List<ValidationResult>) = formState.copy(
        firstNameError = results[0].errorMessage,
        lastNameError = results[1].errorMessage
    )

    override suspend fun getFormResult(): ProfileFormResult {
        val loaded = profileState.value as LoadingState.Loaded
        val profile = loaded.value ?: return ProfileFormResult.Success

        val uploadedAvatarUrl = formState.avatarContent.run {
            if (this.isEmpty()) return@run ""

            useCases.uploadAvatar(
                path = profile.id,
                content = formState.avatarContent
            )
        }

        val newProfile = profile.copy(
            firstName = formState.firstName,
            lastName = formState.lastName,
            avatarUrl = uploadedAvatarUrl.ifBlank { profile.avatarUrl }
        )
        useCases.updateProfile(newProfile)

        return ProfileFormResult.Success
    }
}