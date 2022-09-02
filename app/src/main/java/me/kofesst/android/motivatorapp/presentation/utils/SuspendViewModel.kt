package me.kofesst.android.motivatorapp.presentation.utils

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class SuspendViewModel : ViewModel() {
    private val _loadingState = mutableStateOf(false)
    val loadingState: State<Boolean> = _loadingState

    private var _blockingJob: Job? = null

    private fun launchBlock(
        afterRun: () -> Unit = {},
        block: suspend () -> Unit,
    ): Job? = if (_blockingJob?.isActive != true) {
        viewModelScope.launch {
            _loadingState.value = true
            block()
            _loadingState.value = false
            afterRun()
        }
    } else {
        _blockingJob
    }

    protected fun runSuspend(
        afterRun: () -> Unit = {},
        block: suspend () -> Unit,
    ) {
        launchBlock(afterRun, block)
    }

    protected fun runBlockingSuspend(
        afterRun: () -> Unit = {},
        block: suspend () -> Unit,
    ) {
        _blockingJob = launchBlock(afterRun, block)
    }
}