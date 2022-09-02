package me.kofesst.android.motivatorapp.presentation.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import me.kofesst.android.motivatorapp.presentation.utils.UiText

data class BottomBarSettings(
    val visible: Boolean = false,
    val icon: ImageVector = Icons.Outlined.Star,
    val title: UiText = UiText.Static(""),
)