package me.kofesst.android.motivatorapp.presentation.utils

import androidx.compose.runtime.Composable
import me.kofesst.android.motivatorapp.presentation.utils.AppText.Case.postsFirstCase
import me.kofesst.android.motivatorapp.presentation.utils.AppText.Case.postsSecondCase
import me.kofesst.android.motivatorapp.presentation.utils.AppText.Case.postsThirdCase
import me.kofesst.android.motivatorapp.presentation.utils.AppText.Case.ratingFirstCase
import me.kofesst.android.motivatorapp.presentation.utils.AppText.Case.ratingSecondCase
import me.kofesst.android.motivatorapp.presentation.utils.AppText.Case.ratingThirdCase

data class WordCases(
    val firstCase: String,
    val secondCase: String,
    val thirdCase: String,
) {
    fun getCase(amount: Int): String {
        if (amount % 100 in 11..19) return thirdCase

        return when (amount % 10) {
            1 -> firstCase
            in 2..4 -> secondCase
            else -> thirdCase
        }
    }
}

data class UiWordCases(
    val firstCase: UiText,
    val secondCase: UiText,
    val thirdCase: UiText,
) {
    @Composable
    fun getCase(amount: Int): String = WordCases(
        firstCase = firstCase(),
        secondCase = secondCase(),
        thirdCase = thirdCase()
    ).getCase(amount)
}

val postsCases = UiWordCases(postsFirstCase, postsSecondCase, postsThirdCase)
val ratingCases = UiWordCases(ratingFirstCase, ratingSecondCase, ratingThirdCase)