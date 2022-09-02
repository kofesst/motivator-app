package me.kofesst.android.motivatorapp.domain.usecases.validation

/**
 * Класс, представляющий результат проверки значения
 * юзкейсами валидации.
 */
sealed class ValidationResult {
    /**
     * Результат того, что значение пустое.
     */
    object EmptyField : ValidationResult()

    /**
     * Результат того, что длина значения маленькая.
     */
    data class SmallLength(val minLength: Int) : ValidationResult()

    /**
     * Результат того, что длина значения большая.
     */
    data class LongLength(val maxLength: Int) : ValidationResult()

    /**
     * Результат того, что значение не является email.
     */
    object InvalidEmail : ValidationResult()

    /**
     * Результат того, что в значении нет цифр.
     */
    object PasswordNeedDigit : ValidationResult()

    /**
     * Результат того, что в значении нет букв.
     */
    object PasswordNeedLetters : ValidationResult()

    /**
     * Результат того, что в значении есть пробельные символы.
     */
    object PasswordContainsSpace : ValidationResult()

    /**
     * Результат того, что значение прошло все проверки.
     */
    object Success : ValidationResult()

    /**
     * Преобразует новый результат валидации из двух:
     * - Если первый результат не равен [Success], то возвращает себя.
     * - Если первый результат равен [Success], а второй нет, то возвращает [other].
     */
    operator fun plus(other: ValidationResult): ValidationResult {
        if (this is Success) return other
        return this
    }
}