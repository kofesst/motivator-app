package me.kofesst.android.motivatorapp.presentation.utils

private val counterSuffixes = mapOf(
    1_000 to "K",
    1_000_000 to "M"
)

fun Int.asCounter(): String {
    if (this == Int.MIN_VALUE) return Int.MIN_VALUE.inc().asCounter()
    if (this < 0) return "-${this.unaryMinus().asCounter()}"
    if (this < 1000) return this.toString()

    val suffixEntry = counterSuffixes.entries.last { entry ->
        val minimum = entry.key
        this@asCounter >= minimum
    }
    val divideBy = suffixEntry.key
    val suffix = suffixEntry.value

    val truncated = this / (divideBy / 10)
    val hasFloat = truncated % 10 != 0

    return buildString {
        append(truncated / 10)
        if (hasFloat) {
            append(",")
            append(truncated % 10)
        }
        append(suffix)
    }
}