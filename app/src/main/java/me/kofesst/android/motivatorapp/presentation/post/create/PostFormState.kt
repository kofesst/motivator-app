package me.kofesst.android.motivatorapp.presentation.post.create

import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.presentation.utils.UiText

data class PostFormState(
    val title: String = "",
    val titleError: UiText? = null,
    val description: String = "",
    val descriptionError: UiText? = null,
    val difficulty: MotivationPost.Difficulty = MotivationPost.Difficulty.Easy,
)
