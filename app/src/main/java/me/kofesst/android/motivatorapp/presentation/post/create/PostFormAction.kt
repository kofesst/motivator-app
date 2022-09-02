package me.kofesst.android.motivatorapp.presentation.post.create

import me.kofesst.android.motivatorapp.domain.models.MotivationPost

sealed class PostFormAction {
    data class TitleChanged(val title: String) : PostFormAction()
    data class DescriptionChanged(val description: String) : PostFormAction()
    data class DifficultyChanged(val difficulty: MotivationPost.Difficulty) : PostFormAction()

    object Submit : PostFormAction()
}
