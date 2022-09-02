package me.kofesst.android.motivatorapp.domain.usecases.validation

/**
 * Юзкейс, проверяющий на длину значения.
 */
class ValidateForLength {
    /**
     * Возвращает [ValidationResult.Success], если длина [value] находится
     * в диапазоне подходящих длин [lengthRange],
     * [ValidationResult.SmallLength], если длина значения меньше подходящей,
     * и [ValidationResult.LongLength], если длина значения больше подходящей.
     *
     * [value] - проверяемое значение.
     *
     * [lengthRange] - диапазон подходящих длин значения.
     */
    operator fun invoke(value: String, lengthRange: IntRange) = value.length.run {
        if (this < lengthRange.first) {
            ValidationResult.SmallLength(lengthRange.first)
        } else if (this > lengthRange.last) {
            ValidationResult.LongLength(lengthRange.last)
        } else {
            ValidationResult.Success
        }
    }
}