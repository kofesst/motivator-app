package me.kofesst.android.motivatorapp.domain

import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import me.kofesst.android.motivatorapp.domain.models.UserProfile
import me.kofesst.android.motivatorapp.domain.models.UserProfileWithPosts
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Stream
import kotlin.random.Random

/**
 * Unit тесты, проверяющие на корректность
 * значения модели [UserProfileWithPosts].
 */
class ProfileWithPostsTest {
    companion object {
        private val testProfile = UserProfile(
            id = "testId",
            firstName = "testName",
            lastName = "testName",
            avatarUrl = ""
        )

        /**
         * Создает случайный набор записей со случайной
         * сложностью и возвращает сумму рейтинга всех этих
         * записей.
         */
        @JvmStatic
        private fun createRandomPosts(maxCount: Int = 10): Pair<List<MotivationPost>, Int> {
            var rating = 0
            return List(maxCount) {
                val difficultyIndex = Random.nextInt(MotivationPost.Difficulty.values().size)
                val difficulty = MotivationPost.Difficulty.values()[difficultyIndex]
                val expired = Random.nextBoolean()
                val timestamp = SimpleDateFormat("dd.MM.yyyy").parse("10.01.2020")!!
                val deadline = if (expired) {
                    Date(timestamp.time)
                } else {
                    Date(Date().time + 1000 * 60 * 60 * 24)
                }
                val completed = Random.nextBoolean()

                rating += if (completed) {
                    difficulty.rating
                } else if (expired) {
                    -difficulty.rating
                } else {
                    0
                }

                MotivationPost(
                    title = "TestPost",
                    description = "TestDesc",
                    difficulty = difficulty,
                    timestamp = timestamp,
                    deadline = deadline,
                    completed = completed
                )
            } to rating
        }

        /**
         * Параметры для Unit теста случайной проверки userRating.
         */
        @JvmStatic
        fun randomTestParameters(): Stream<Arguments> = Stream.of(
            createRandomPosts().run {
                Arguments.of(first, second)
            }
        )
    }

    /**
     * Unit тест, проверяющий правильность подсчета
     * [UserProfileWithPosts.userRating].
     */
    @ParameterizedTest
    @MethodSource("randomTestParameters")
    fun `random userRating works as expected`(posts: List<MotivationPost>, expectedRating: Int) {
        val profileWithPosts = UserProfileWithPosts(
            profile = testProfile,
            posts = posts
        )
        val actualRating = profileWithPosts.userRating
        assertEquals(expectedRating, actualRating)
    }
}