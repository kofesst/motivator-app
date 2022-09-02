package me.kofesst.android.motivatorapp.domain.usecases.validation

/**
 * Юзкейс, проверяющий корректность пароля.
 */
class ValidateForPassword {
    /**
     * Возвращает:
     * - [ValidationResult.PasswordNeedDigit], если в [value] нет цифр.
     * - [ValidationResult.PasswordNeedLetters], если в [value] нет букв.
     * - [ValidationResult.PasswordContainsSpace], если в [value] есть пробельные символы.
     * - [ValidationResult.Success], если [value] прошло все проверки.
     *
     * [value] - проверяемое значение.
     */
    operator fun invoke(value: String) = if (!value.contains(Regex("\\d"))) {
        ValidationResult.PasswordNeedDigit
    } else if (!value.contains(Regex("[а-яА-Яa-zA-Z]"))) {
        ValidationResult.PasswordNeedLetters
    } else if (value.contains(Regex("\\s"))) {
        ValidationResult.PasswordContainsSpace
    } else {
        ValidationResult.Success
    }
}