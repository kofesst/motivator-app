package me.kofesst.android.motivatorapp.presentation.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidationResult

abstract class FormViewModel<FormState : Any, FormResult : Any, FormAction : Any>(
    initialFormState: FormState,
) : SuspendViewModel() {
    var formState by mutableStateOf(initialFormState)

    private val validationChannel = Channel<FormResult>()
    val formResult = validationChannel.receiveAsFlow()

    abstract fun onFormAction(action: FormAction)

    protected abstract fun validateFields(): List<ValidationResult>
    protected abstract fun validationResultsToFormState(results: List<ValidationResult>): FormState
    protected abstract suspend fun getFormResult(): FormResult

    protected fun onSubmit() {
        val validationResults = validateFields()
        formState = validationResultsToFormState(validationResults)

        val hasError = validationResults.any { it !is ValidationResult.Success }
        if (hasError) return

        runBlockingSuspend {
            val result = getFormResult()
            validationChannel.send(result)
        }
    }
}