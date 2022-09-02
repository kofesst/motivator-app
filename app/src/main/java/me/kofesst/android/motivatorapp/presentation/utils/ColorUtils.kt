package me.kofesst.android.motivatorapp.presentation.utils

import androidx.compose.ui.graphics.Color
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.ui.theme.DeepOrange
import me.kofesst.android.motivatorapp.ui.theme.LightGreen
import me.kofesst.android.motivatorapp.ui.theme.Yellow

val MotivationPost.Difficulty.color: Color
    get() = when (this) {
        MotivationPost.Difficulty.Easy -> LightGreen
        MotivationPost.Difficulty.Normal -> Yellow
        MotivationPost.Difficulty.Hard -> DeepOrange
    }