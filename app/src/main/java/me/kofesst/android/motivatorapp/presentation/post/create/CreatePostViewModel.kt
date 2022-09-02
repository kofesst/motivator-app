package me.kofesst.android.motivatorapp.presentation.post.create

import dagger.hilt.android.lifecycle.HiltViewModel
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.constants.AppConstants
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidationResult
import me.kofesst.android.motivatorapp.presentation.utils.FormViewModel
import me.kofesst.android.motivatorapp.presentation.utils.addDays
import me.kofesst.android.motivatorapp.presentation.utils.errorMessage
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreatePostViewModel @Inject constructor(
    private val useCases: UseCases,
) : FormViewModel<PostFormState, PostFormResult, PostFormAction>(
    initialFormState = PostFormState()
) {
    override fun onFormAction(action: PostFormAction) {
        formState = when (action) {
            is PostFormAction.TitleChanged -> {
                formState.copy(title = action.title)
            }
            is PostFormAction.DescriptionChanged -> {
                formState.copy(description = action.description)
            }
            is PostFormAction.DifficultyChanged -> {
                formState.copy(difficulty = action.difficulty)
            }
            PostFormAction.Submit -> {
                onSubmit()
                return
            }
        }
    }

    override fun validateFields(): List<ValidationResult> {
        val titleResult = useCases.validateForEmptyField(
            value = formState.title
        ) + useCases.validateForLength(
            value = formState.title,
            lengthRange = AppConstants.Posts.TITLE_LENGTH_RANGE
        )
        val descriptionResult = useCases.validateForEmptyField(
            value = formState.description
        ) + useCases.validateForLength(
            value = formState.description,
            lengthRange = AppConstants.Posts.DESCRIPTION_LENGTH_RANGE
        )

        return listOf(
            titleResult,
            descriptionResult
        )
    }

    override fun validationResultsToFormState(results: List<ValidationResult>) = formState.copy(
        titleError = results[0].errorMessage,
        descriptionError = results[1].errorMessage
    )

    override suspend fun getFormResult(): PostFormResult {
        val post = MotivationPost(
            title = formState.title,
            description = formState.description,
            difficulty = formState.difficulty,
            timestamp = Date(),
            deadline = Date().addDays(7)
        )
        useCases.createPost(post)
        return PostFormResult.Success
    }
}