package me.kofesst.android.motivatorapp.data.models

import me.kofesst.android.motivatorapp.data.models.utils.DomainDeserializer
import me.kofesst.android.motivatorapp.data.models.utils.DomainSerializable
import me.kofesst.android.motivatorapp.domain.models.UserProfile

/**
 * Data Transfer Object для модели [UserProfile].
 */
data class UserProfileDto(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val avatarUrl: String = "",
    val rating: Int = 0,
) : DomainSerializable<UserProfile> {
    companion object : DomainDeserializer<UserProfile, UserProfileDto> {
        override fun fromDomain(domain: UserProfile) = domain.run {
            UserProfileDto(
                id = id,
                firstName = firstName,
                lastName = lastName,
                avatarUrl = avatarUrl
            )
        }
    }

    override fun toDomain() = UserProfile(
        id = id,
        firstName = firstName,
        lastName = lastName,
        avatarUrl = avatarUrl
    )
}