package uvg.plats.fixerly.data.model

import com.google.firebase.firestore.PropertyName

/**
 * Representa la respuesta de un proveedor a una solicitud
 * Se guarda como campo dentro de ServiceRequest
 */
data class ProviderResponse(
    @PropertyName("providerId")
    val providerId: String = "",

    @PropertyName("providerName")
    val providerName: String = "",

    @PropertyName("providerEmail")
    val providerEmail: String = "",

    @PropertyName("providerPhone")
    val providerPhone: String = "",

    @PropertyName("skills")
    val skills: List<String> = emptyList(),

    @PropertyName("message")
    val message: String = "", // Mensaje opcional del proveedor

    @PropertyName("contactPreferences")
    val contactPreferences: List<String> = emptyList(), // ["email", "telefono"]

    @PropertyName("status")
    val status: String = "pending", // "pending", "accepted", "rejected"

    @PropertyName("respondedAt")
    val respondedAt: Long = System.currentTimeMillis()
) {
    // Constructor sin argumentos para Firestore
    constructor() : this(
        providerId = "",
        providerName = "",
        providerEmail = "",
        providerPhone = "",
        skills = emptyList(),
        message = "",
        contactPreferences = emptyList(),
        status = "pending",
        respondedAt = System.currentTimeMillis()
    )

    /**
     * Verificar si fue aceptado
     */
    fun isAccepted(): Boolean = status == "accepted"

    /**
     * Verificar si fue rechazado
     */
    fun isRejected(): Boolean = status == "rejected"

    /**
     * Verificar si está pendiente
     */
    fun isPending(): Boolean = status == "pending"

    /**
     * Convertir a Map para Firestore
     */
    fun toMap(): Map<String, Any> {
        return mapOf(
            "providerId" to providerId,
            "providerName" to providerName,
            "providerEmail" to providerEmail,
            "providerPhone" to providerPhone,
            "skills" to skills,
            "message" to message,
            "contactPreferences" to contactPreferences,
            "status" to status,
            "respondedAt" to respondedAt
        )
    }

    companion object {
        /**
         * Crear desde Map (Firestore)
         */
        fun fromMap(map: Map<String, Any>?): ProviderResponse? {
            if (map == null) return null

            return ProviderResponse(
                providerId = map["providerId"] as? String ?: "",
                providerName = map["providerName"] as? String ?: "",
                providerEmail = map["providerEmail"] as? String ?: "",
                providerPhone = map["providerPhone"] as? String ?: "",
                skills = (map["skills"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                message = map["message"] as? String ?: "",
                contactPreferences = (map["contactPreferences"] as? List<*>)?.filterIsInstance<String>() ?: emptyList(),
                status = map["status"] as? String ?: "pending",
                respondedAt = map["respondedAt"] as? Long ?: System.currentTimeMillis()
            )
        }

        /**
         * Crear respuesta desde User proveedor
         */
        fun fromUser(user: User, message: String = ""): ProviderResponse {
            return ProviderResponse(
                providerId = user.userId,
                providerName = user.getFullName(),
                providerEmail = user.email,
                providerPhone = user.phone,
                skills = user.skills,
                message = message,
                contactPreferences = user.contactPreferences,
                status = "pending"
            )
        }
    }
}