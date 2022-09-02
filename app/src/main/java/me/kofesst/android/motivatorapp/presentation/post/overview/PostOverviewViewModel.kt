package me.kofesst.android.motivatorapp.presentation.post.overview

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.presentation.utils.LoadingState
import javax.inject.Inject

@HiltViewModel
class PostOverviewViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _postState = mutableStateOf<LoadingState<MotivationPostWithAuthor>>(
        value = LoadingState.Idle()
    )
    val postState: State<LoadingState<MotivationPostWithAuthor>> = _postState

    private val _ownedPostState = mutableStateOf(false)
    val ownedPostState: State<Boolean> get() = _ownedPostState

    fun loadPost(id: String) {
        viewModelScope.launch {
            _postState.value = LoadingState.Loading()
            _postState.value = LoadingState.Loaded(
                value = useCases.getPostWithAuthor(id)?.also {
                    _ownedPostState.value = useCases.getLoggedProfileId() == it.author.id
                }
            )
        }
    }

    fun completePost(
        post: MotivationPost,
        onSaved: () -> Unit,
    ) {
        viewModelScope.launch {
            post.completed = true
            useCases.updatePost(post)
            onSaved()
        }
    }
}