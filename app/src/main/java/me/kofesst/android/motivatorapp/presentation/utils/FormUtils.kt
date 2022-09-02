package me.kofesst.android.motivatorapp.presentation.utils

import me.kofesst.android.motivatorapp.domain.models.results.AuthResult
import me.kofesst.android.motivatorapp.domain.usecases.validation.ValidationResult

val ValidationResult.errorMessage: UiText?
    get() = when (this) {
        ValidationResult.EmptyField -> AppText.Error.emptyFieldError
        is ValidationResult.SmallLength -> AppText.Error.smallLengthError.apply {
            this.defaultFormats = listOf(this@errorMessage.minLength)
        }
        is ValidationResult.LongLength -> AppText.Error.longLengthError.apply {
            this.defaultFormats = listOf(this@errorMessage.maxLength)
        }
        ValidationResult.InvalidEmail -> AppText.Error.invalidEmailError
        ValidationResult.PasswordContainsSpace -> AppText.Error.passwordContainsSpaceError
        ValidationResult.PasswordNeedDigit -> AppText.Error.passwordNeedDigitError
        ValidationResult.PasswordNeedLetters -> AppText.Error.passwordNeedLetterError
        ValidationResult.Success -> null
    }

val AuthResult.Failed.errorMessage: UiText
    get() = when (this) {
        AuthResult.Failed.EmailDoesNotExist -> AppText.Error.emailDoesNotExistError
        AuthResult.Failed.EmailAlreadyExists -> AppText.Error.emailAlreadyExistsError
        AuthResult.Failed.IncorrectPassword -> AppText.Error.incorrectPasswordError
        AuthResult.Failed.EmailAlreadyExists -> AppText.Error.emailAlreadyExistsError
        is AuthResult.Failed.Unexpected -> UiText.Static(exception.toString())
    }