package me.kofesst.android.motivatorapp.domain.usecases.validation

import android.util.Patterns

/**
 * Юзкейс, проверяющий email на корректность.
 */
class ValidateForEmail {
    /**
     * Возвращает [ValidationResult.Success], если [value] является email,
     * и [ValidationResult.InvalidEmail] в обратном случае.
     *
     * [value] - проверяемое значение.
     */
    operator fun invoke(value: String) = if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
        ValidationResult.Success
    } else {
        ValidationResult.InvalidEmail
    }
}