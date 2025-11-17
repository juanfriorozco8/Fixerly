package uvg.plats.fixerly.data.model

import com.google.firebase.firestore.PropertyName
import uvg.plats.fixerly.data.local.entities.ServiceRequestEntity
import java.util.UUID

data class ServiceRequest(
    @PropertyName("requestId")
    val requestId: String = UUID.randomUUID().toString(),

    @PropertyName("clientId")
    val clientId: String = "",

    @PropertyName("clientName")
    val clientName: String = "",

    @PropertyName("serviceType")
    val serviceType: String = "", // "plomeria", "electricidad", etc.

    @PropertyName("description")
    val description: String = "",

    @PropertyName("address")
    val address: Address? = null,

    @PropertyName("status")
    val status: String = "pending", // "pending", "in_progress", "completed", "cancelled"

    @PropertyName("responses")
    val responses: List<ProviderResponse> = emptyList(), // Lista de respuestas de proveedores

    @PropertyName("acceptedProviderId")
    val acceptedProviderId: String? = null, // ID del proveedor aceptado

    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis(),

    @PropertyName("updatedAt")
    val updatedAt: Long = System.currentTimeMillis()
) {
    // Constructor sin argumentos para Firestore
    constructor() : this(
        requestId = UUID.randomUUID().toString(),
        clientId = "",
        clientName = "",
        serviceType = "",
        description = "",
        address = null,
        status = "pending",
        responses = emptyList(),
        acceptedProviderId = null,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    /**
     * Convertir a entidad de Room para caché local
     */
    fun toEntity(): ServiceRequestEntity {
        return ServiceRequestEntity(
            id = requestId,
            clientId = clientId,
            categoria = serviceType,
            descripcionBreve = serviceType,
            descripcionDetallada = description,
            estado = status,
            createdAt = createdAt
        )
    }

    /**
     * Verificar si está pendiente
     */
    fun isPending(): Boolean = status == "pending"

    /**
     * Verificar si está en progreso
     */
    fun isInProgress(): Boolean = status == "in_progress"

    /**
     * Verificar si está completada
     */
    fun isCompleted(): Boolean = status == "completed"

    /**
     * Verificar si tiene respuestas
     */
    fun hasResponses(): Boolean = responses.isNotEmpty()

    /**
     * Obtener número de respuestas pendientes
     */
    fun getPendingResponsesCount(): Int {
        return responses.count { it.isPending() }
    }

    /**
     * Obtener respuesta aceptada
     */
    fun getAcceptedResponse(): ProviderResponse? {
        return responses.firstOrNull { it.isAccepted() }
    }

    /**
     * Convertir a Map para Firestore
     */
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>(
            "requestId" to requestId,
            "clientId" to clientId,
            "clientName" to clientName,
            "serviceType" to serviceType,
            "description" to description,
            "status" to status,
            "responses" to responses.map { it.toMap() },
            "createdAt" to createdAt,
            "updatedAt" to updatedAt
        )

        if (address != null) {
            map["address"] = address.toMap()
        }

        if (acceptedProviderId != null) {
            map["acceptedProviderId"] = acceptedProviderId
        }

        return map
    }

    companion object {
        /**
         * Crear desde entidad de Room
         */
        fun fromEntity(entity: ServiceRequestEntity): ServiceRequest {
            return ServiceRequest(
                requestId = entity.id,
                clientId = entity.clientId,
                serviceType = entity.categoria,
                description = entity.descripcionDetallada,
                status = entity.estado,
                createdAt = entity.createdAt
            )
        }

        /**
         * Crear desde Map (Firestore)
         */
        fun fromMap(map: Map<String, Any>?): ServiceRequest? {
            if (map == null) return null

            val responsesList = (map["responses"] as? List<*>)
                ?.mapNotNull { it as? Map<String, Any> }
                ?.mapNotNull { ProviderResponse.fromMap(it) }
                ?: emptyList()

            val addressMap = map["address"] as? Map<String, Any>

            return ServiceRequest(
                requestId = map["requestId"] as? String ?: "",
                clientId = map["clientId"] as? String ?: "",
                clientName = map["clientName"] as? String ?: "",
                serviceType = map["serviceType"] as? String ?: "",
                description = map["description"] as? String ?: "",
                address = Address.fromMap(addressMap),
                status = map["status"] as? String ?: "pending",
                responses = responsesList,
                acceptedProviderId = map["acceptedProviderId"] as? String,
                createdAt = map["createdAt"] as? Long ?: System.currentTimeMillis(),
                updatedAt = map["updatedAt"] as? Long ?: System.currentTimeMillis()
            )
        }
    }
}