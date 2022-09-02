package me.kofesst.android.motivatorapp.data.models

import me.kofesst.android.motivatorapp.data.models.utils.DomainDeserializer
import me.kofesst.android.motivatorapp.data.models.utils.DomainSerializable
import me.kofesst.android.motivatorapp.domain.models.MotivationPost
import java.util.*

/**
 * Data Transfer Object для модели [MotivationPost].
 */
data class MotivationPostDto(
    val id: String = "",
    val authorId: String = "",
    val title: String = "",
    val description: String = "",
    val difficultyId: Int = 0,
    val timestamp: Long = 0,
    val deadline: Long = 0,
    val completed: Boolean = false,
) : DomainSerializable<MotivationPost> {
    companion object : DomainDeserializer<MotivationPost, MotivationPostDto> {
        override fun fromDomain(domain: MotivationPost) = domain.run {
            MotivationPostDto(
                id = id,
                authorId = authorId,
                title = title,
                description = description,
                difficultyId = difficulty.ordinal,
                timestamp = timestamp.time,
                deadline = deadline.time,
                completed = completed
            )
        }
    }

    override fun toDomain() = MotivationPost(
        id = id,
        authorId = authorId,
        title = title,
        description = description,
        difficulty = MotivationPost.Difficulty.values()[difficultyId],
        timestamp = Date(timestamp),
        deadline = Date(deadline),
        completed = completed
    )
}