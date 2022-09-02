package me.kofesst.android.motivatorapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import me.kofesst.android.motivatorapp.presentation.utils.SuspendViewModel

@Composable
fun LoadingHandler(
    viewModel: SuspendViewModel,
) {
    val isLoading by viewModel.loadingState
    if (isLoading) {
        LoadingPanel()
    }
}