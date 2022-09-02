package me.kofesst.android.motivatorapp.domain.usecases.validation

/**
 * Юзкейс, проверяющий на пустое значение.
 */
class ValidateForEmptyField {
    /**
     * Возвращает [ValidationResult.Success], если [value] не пустое,
     * и [ValidationResult.EmptyField] в обратном случае.
     *
     * [value] - проверяемое значение.
     */
    operator fun invoke(value: String) = if (value.isBlank()) {
        ValidationResult.EmptyField
    } else {
        ValidationResult.Success
    }
}