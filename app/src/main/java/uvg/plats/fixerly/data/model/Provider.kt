package uvg.plats.fixerly.data.model

data class Provider(
    val userId: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val profileImageUrl: String = "",
    val skills: List<String> = emptyList(),
    val about: String = "",
    val contactPreferences: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),

    val rating: Double = 0.0,
    val ratingCount: Int = 0
) {
    fun getFullName(): String = "$name $lastName"

    fun hasSkill(skill: String): Boolean =
        skills.any { it.equals(skill, ignoreCase = true) }

    fun getSkillsFormatted(): String =
        skills.joinToString(", ")

    fun toUser(): User {
        return User(
            userId = userId,
            name = name,
            lastName = lastName,
            email = email,
            phone = phone,
            userType = "provider",
            profileImageUrl = profileImageUrl,
            skills = skills,
            about = about,
            contactPreferences = contactPreferences,
            createdAt = createdAt,

            rating = rating,
            ratingCount = ratingCount
        )
    }

    companion object {
        fun fromUser(user: User): Provider? {
            if (!user.isProvider()) return null

            return Provider(
                userId = user.userId,
                name = user.name,
                lastName = user.lastName,
                email = user.email,
                phone = user.phone,
                profileImageUrl = user.profileImageUrl,
                skills = user.skills,
                about = user.about,
                contactPreferences = user.contactPreferences,
                createdAt = user.createdAt,
                rating = user.rating,
                ratingCount = user.ratingCount
            )
        }
    }
}
