package me.kofesst.android.motivatorapp.presentation.profile.overview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.kofesst.android.motivatorapp.domain.models.UserProfileWithPosts
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.presentation.utils.LoadingState
import javax.inject.Inject

@HiltViewModel
class ProfileOverviewViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _profileState = mutableStateOf<LoadingState<UserProfileWithPosts>>(
        value = LoadingState.Idle()
    )
    val profileState: State<LoadingState<UserProfileWithPosts>> = _profileState

    private val _isSelfProfile = mutableStateOf(false)
    val isSelfProfile: State<Boolean> = _isSelfProfile

    fun loadProfile(id: String) {
        viewModelScope.launch {
            _profileState.value = LoadingState.Loading()

            val profileId = id.ifBlank { useCases.getLoggedProfileId() }
            _profileState.value = if (profileId == null) {
                LoadingState.Failed(
                    exception = NullPointerException("Auth can't find logged user uid")
                )
            } else {
                LoadingState.Loaded(
                    value = useCases.getProfileWithPosts(profileId)
                )
            }

            _isSelfProfile.value = if (id.isNotBlank()) {
                id == profileId
            } else {
                true
            }
        }
    }
}