package me.kofesst.android.motivatorapp.presentation

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.presentation.utils.SuspendViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCases: UseCases,
) : SuspendViewModel() {
    fun clearSession(onCleared: () -> Unit) {
        runSuspend(afterRun = onCleared) {
            useCases.clearSession()
        }
    }
}