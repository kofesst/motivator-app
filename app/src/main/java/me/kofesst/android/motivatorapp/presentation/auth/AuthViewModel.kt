package me.kofesst.android.motivatorapp.presentation.auth

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import me.kofesst.android.motivatorapp.domain.models.constants.AppConstants
import me.kofesst.android.motivatorapp.domain.models.results.AuthResult
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidationResult
import me.kofesst.android.motivatorapp.presentation.utils.FormViewModel
import me.kofesst.android.motivatorapp.presentation.utils.errorMessage
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val useCases: UseCases,
) : FormViewModel<AuthFormState, AuthResult, AuthFormAction>(
    initialFormState = AuthFormState()
) {
    private val _screenState = mutableStateOf(AuthScreenState.SignIn)
    val screenState: State<AuthScreenState> = _screenState

    private val _sessionCheckState = mutableStateOf(true)
    val sessionCheckState: State<Boolean> get() = _sessionCheckState

    fun tryRestoreSession() {
        var session: Pair<String, String>? = null
        runSuspend(
            afterRun = {
                session?.run {
                    formState = formState.copy(
                        email = this.first,
                        password = this.second
                    )
                    onSubmit()
                } ?: kotlin.run {
                    _sessionCheckState.value = false
                }
            }
        ) {
            session = useCases.restoreSession()
        }
    }

    fun toggleScreenState() {
        _screenState.value = screenState.value.opposite()
    }

    override fun onFormAction(action: AuthFormAction) {
        formState = when (action) {
            is AuthFormAction.EmailChanged -> {
                formState.copy(email = action.email)
            }
            is AuthFormAction.PasswordChanged -> {
                formState.copy(password = action.password)
            }
            is AuthFormAction.FirstNameChanged -> {
                formState.copy(firstName = action.firstName)
            }
            is AuthFormAction.LastNameChanged -> {
                formState.copy(lastName = action.lastName)
            }
            is AuthFormAction.RememberSessionChanged -> {
                formState.copy(rememberSession = action.value)
            }
            AuthFormAction.Submit -> {
                onSubmit()
                return
            }
        }
    }

    override fun validateFields(): List<ValidationResult> {
        val emailResult = useCases.validateForEmail(
            value = formState.email
        )
        val passwordResult = useCases.validateForLength(
            value = formState.password,
            lengthRange = AppConstants.Profile.PASSWORD_LENGTH_RANGE
        ) + useCases.validateForPassword(
            value = formState.password
        )
        val (firstNameResult, lastNameResult) = if (screenState.value == AuthScreenState.SignUp) {
            useCases.validateForLength(
                value = formState.firstName,
                lengthRange = AppConstants.Profile.NAME_LENGTH_RANGE
            ) to useCases.validateForLength(
                value = formState.firstName,
                lengthRange = AppConstants.Profile.NAME_LENGTH_RANGE
            )
        } else {
            ValidationResult.Success to ValidationResult.Success
        }

        return listOf(
            emailResult,
            passwordResult,
            firstNameResult,
            lastNameResult
        )
    }

    override fun validationResultsToFormState(results: List<ValidationResult>) = formState.copy(
        emailError = results[0].errorMessage,
        passwordError = results[1].errorMessage,
        firstNameError = results[2].errorMessage,
        lastNameError = results[3].errorMessage
    )

    override suspend fun getFormResult(): AuthResult {
        return when (screenState.value) {
            AuthScreenState.SignUp -> {
                useCases.registerUser(
                    email = formState.email,
                    password = formState.password,
                    firstName = formState.firstName,
                    lastName = formState.lastName,
                    rememberSession = formState.rememberSession
                )
            }
            AuthScreenState.SignIn -> {
                useCases.signInUser(
                    email = formState.email,
                    password = formState.password,
                    rememberSession = formState.rememberSession
                )
            }
        }
    }
}