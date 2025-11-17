package uvg.plats.fixerly.data.repository

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.ProviderResponse
import uvg.plats.fixerly.data.model.ServiceRequest
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.utils.FirebaseConstants

class ServiceRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Crear una nueva solicitud de servicio (CLIENTE)
     */
    suspend fun createServiceRequest(
        clientId: String,
        clientName: String,
        serviceType: String,
        description: String,
        address: uvg.plats.fixerly.data.model.Address
    ): Result<String> {
        return try {
            val request = ServiceRequest(
                clientId = clientId,
                clientName = clientName,
                serviceType = serviceType,
                description = description,
                address = address,
                status = FirebaseConstants.REQUEST_STATUS_PENDING,
                createdAt = Timestamp.now()
            )

            val docRef = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .add(request)
                .await()

            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener todas las solicitudes pendientes (PROVEEDOR)
     * Escucha cambios en tiempo real
     */
    fun getAllPendingRequests(): Flow<List<ServiceRequest>> = callbackFlow {
        val listener = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
            .whereEqualTo(FirebaseConstants.FIELD_STATUS, FirebaseConstants.REQUEST_STATUS_PENDING)
            .orderBy(FirebaseConstants.FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(ServiceRequest::class.java)?.copy(requestId = doc.id)
                } ?: emptyList()

                trySend(requests)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Obtener solicitudes de un cliente específico
     */
    fun getClientRequests(clientId: String): Flow<List<ServiceRequest>> = callbackFlow {
        val listener = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
            .whereEqualTo(FirebaseConstants.FIELD_CLIENT_ID, clientId)
            .orderBy(FirebaseConstants.FIELD_CREATED_AT, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(ServiceRequest::class.java)?.copy(requestId = doc.id)
                } ?: emptyList()

                trySend(requests)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Proveedor envía su info a una solicitud
     */
    suspend fun respondToRequest(
        requestId: String,
        providerUser: User,
        message: String = ""
    ): Result<Unit> {
        return try {
            val response = ProviderResponse(
                providerId = providerUser.userId,
                providerName = providerUser.fullName(),
                providerPhone = providerUser.phone,
                providerEmail = providerUser.email,
                providerSkills = providerUser.skills,
                providerAbout = providerUser.about,
                providerImageUrl = providerUser.profileImageUrl,
                message = message,
                status = "pending",
                respondedAt = Timestamp.now()
            )

            // Agregar la respuesta al array de responses
            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .update(
                    FirebaseConstants.FIELD_RESPONSES,
                    com.google.firebase.firestore.FieldValue.arrayUnion(response.toMap())
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cliente acepta la respuesta de un proveedor
     */
    suspend fun acceptProviderResponse(
        requestId: String,
        providerId: String
    ): Result<Unit> {
        return try {
            // Obtener la solicitud actual
            val requestDoc = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .get()
                .await()

            val request = requestDoc.toObject(ServiceRequest::class.java)
                ?: throw Exception("Solicitud no encontrada")

            // Actualizar el array de responses
            val updatedResponses = request.responses.map { response ->
                if (response.providerId == providerId) {
                    response.copy(status = "accepted")
                } else {
                    response
                }
            }

            // Actualizar la solicitud
            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .update(
                    mapOf(
                        FirebaseConstants.FIELD_STATUS to FirebaseConstants.REQUEST_STATUS_ACCEPTED,
                        "acceptedProviderId" to providerId,
                        FirebaseConstants.FIELD_RESPONSES to updatedResponses.map { it.toMap() }
                    )
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Cliente rechaza la respuesta de un proveedor
     */
    suspend fun rejectProviderResponse(
        requestId: String,
        providerId: String
    ): Result<Unit> {
        return try {
            val requestDoc = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .get()
                .await()

            val request = requestDoc.toObject(ServiceRequest::class.java)
                ?: throw Exception("Solicitud no encontrada")

            // Actualizar el array de responses
            val updatedResponses = request.responses.map { response ->
                if (response.providerId == providerId) {
                    response.copy(status = "rejected")
                } else {
                    response
                }
            }

            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .update(
                    FirebaseConstants.FIELD_RESPONSES,
                    updatedResponses.map { it.toMap() }
                )
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Marcar solicitud como completada
     */
    suspend fun completeRequest(requestId: String): Result<Unit> {
        return try {
            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .update(FirebaseConstants.FIELD_STATUS, FirebaseConstants.REQUEST_STATUS_COMPLETED)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Eliminar solicitud
     */
    suspend fun deleteRequest(requestId: String): Result<Unit> {
        return try {
            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .delete()
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}