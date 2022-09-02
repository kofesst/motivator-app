package me.kofesst.android.motivatorapp.presentation.feed

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import me.kofesst.android.motivatorapp.domain.models.MotivationPostWithAuthor
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.presentation.utils.LoadingState
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val useCases: UseCases,
) : ViewModel() {
    private val _feedState = mutableStateOf<LoadingState<List<MotivationPostWithAuthor>>>(
        value = LoadingState.Idle()
    )
    val feedState: State<LoadingState<List<MotivationPostWithAuthor>>> = _feedState

    fun loadFeed() {
        viewModelScope.launch {
            _feedState.value = LoadingState.Loading()
            _feedState.value = useCases.getLoggedProfileId().run UserId@{
                LoadingState.Loaded(
                    value = useCases.getPostsWithAuthor().filter { post ->
                        post.author.id != this@UserId
                    }
                )
            }
        }
    }
}