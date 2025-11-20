package uvg.plats.fixerly.data.model

import com.google.firebase.firestore.PropertyName
import uvg.plats.fixerly.data.local.entities.UserEntity
import java.util.UUID

// este funciona como un dto mas o menos xd o al menos ese es el concepto base

// campos de propiedades que tiene la clase usuario 

data class User(
    @PropertyName("userId")
    val userId: String = "",

    @PropertyName("name")
    val name: String = "",

    @PropertyName("lastName")
    val lastName: String = "",

    @PropertyName("email")
    val email: String = "",

    @PropertyName("phone")
    val phone: String = "",

    @PropertyName("userType")
    val userType: String = "", // "client" o "provider"

    @PropertyName("profileImageUrl")
    val profileImageUrl: String = "",

    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    // Campos específicos de CLIENTE
    @PropertyName("address")
    val address: Address? = null,

    // Campos específicos de PROVEEDOR
    @PropertyName("contactPreferences")
    val contactPreferences: List<String> = emptyList(), // ["email", "telefono"]

    @PropertyName("about")
    val about: String = "",

    @PropertyName("rating")
    val rating: Double = 0.0,

    @PropertyName("ratingCount")
    val ratingCount: Int = 0,

    @PropertyName("skills")
    val skills: List<String> = emptyList() // ["plomeria", "electricidad"]



) {
    // Constructor sin argumentos requerido por Firestore
    // solo para la db ps
    constructor() : this(
        userId = "",
        name = "",
        lastName = "",
        email = "",
        phone = "",
        userType = "",
        profileImageUrl = "",
        createdAt = System.currentTimeMillis()
    )

    /**
     * Convertir a entidad de Room para caché local
     */
    fun toEntity(passwordHash: String = ""): UserEntity {
        return UserEntity(
            id = userId,
            nombre = name,
            apellidos = lastName,
            email = email,
            telefono = phone,
            accountType = userType,
            passwordHash = passwordHash,
            createdAt = createdAt
        )
    }

    /**
     * Obtener nombre completo
     */
    fun getFullName(): String = "$name $lastName"

    /**
     * Verificar si es cliente
     */
    fun isClient(): Boolean = userType == "client"

    /**
     * Verificar si es proveedor
     */
    fun isProvider(): Boolean = userType == "provider"

    /**
     * Convertir a Map para Firestore
     */
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "userId" to userId,
            "name" to name,
            "lastName" to lastName,
            "email" to email,
            "phone" to phone,
            "userType" to userType,
            "profileImageUrl" to profileImageUrl,
            "createdAt" to createdAt
        )

        // Agregar campos específicos según tipo
        if (isClient() && address != null) {
            map["address"] = address.toMap()
        }

        if (isProvider()) {
            map["contactPreferences"] = contactPreferences
            map["about"] = about
            map["skills"] = skills
        }

        return map
    }

    companion object {
        /**
         * Crear desde entidad de Room
         */
        fun fromEntity(entity: UserEntity): User {
            return User(
                userId = entity.id,
                name = entity.nombre,
                lastName = entity.apellidos,
                email = entity.email,
                phone = entity.telefono,
                userType = entity.accountType,
                createdAt = entity.createdAt
            )
        }
    }
}