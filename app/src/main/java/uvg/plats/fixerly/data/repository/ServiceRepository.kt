package uvg.plats.fixerly.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.ProviderResponse
import uvg.plats.fixerly.data.model.ServiceRequest
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.utils.FirebaseConstants

class ServiceRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Crear nueva solicitud de servicio (CLIENTE)
     */
    suspend fun createServiceRequest(
        clientId: String,
        clientName: String,
        serviceType: String,
        description: String,
        address: Address
    ): Result<String> {
        return try {
            val request = ServiceRequest(
                clientId = clientId,
                clientName = clientName,
                serviceType = serviceType,
                description = description,
                address = address,
                status = "pending"
            )

            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(request.requestId)
                .set(request.toMap())
                .await()

            Result.success(request.requestId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener todas las solicitudes pendientes (PROVEEDOR)
     * Flow para actualizaciones en tiempo real
     */
    fun getAllPendingRequests(): Flow<List<ServiceRequest>> = callbackFlow {
        val listener = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
            .whereEqualTo("status", "pending")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    ServiceRequest.fromMap(doc.data)
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
            .whereEqualTo("clientId", clientId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    ServiceRequest.fromMap(doc.data)
                } ?: emptyList()

                trySend(requests)
            }

        awaitClose { listener.remove() }
    }

    /**
     * Obtener una solicitud específica
     */
    suspend fun getRequestById(requestId: String): Result<ServiceRequest> {
        return try {
            val snapshot = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .get()
                .await()

            val request = ServiceRequest.fromMap(snapshot.data)
                ?: throw Exception("Solicitud no encontrada")

            Result.success(request)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Proveedor responde a una solicitud
     */
    suspend fun respondToRequest(
        requestId: String,
        providerUser: User,
        message: String = ""
    ): Result<Unit> {
        return try {
            // Crear respuesta del proveedor
            val response = ProviderResponse.fromUser(providerUser, message)

            // Agregar la respuesta al array de respuestas en Firestore
            firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)
                .update(
                    "responses", FieldValue.arrayUnion(response.toMap()),
                    "updatedAt", System.currentTimeMillis()
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
            val requestRef = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)

            // Obtener el documento actual
            val snapshot = requestRef.get().await()
            val request = ServiceRequest.fromMap(snapshot.data)
                ?: throw Exception("Solicitud no encontrada")

            // Actualizar estado de las respuestas
            val updatedResponses = request.responses.map { response ->
                if (response.providerId == providerId) {
                    response.copy(status = "accepted")
                } else {
                    response.copy(status = "rejected")
                }
            }

            // Actualizar documento
            requestRef.update(
                mapOf(
                    "responses" to updatedResponses.map { it.toMap() },
                    "acceptedProviderId" to providerId,
                    "status" to "in_progress",
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()

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
            val requestRef = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
                .document(requestId)

            val snapshot = requestRef.get().await()
            val request = ServiceRequest.fromMap(snapshot.data)
                ?: throw Exception("Solicitud no encontrada")

            // Actualizar solo esa respuesta
            val updatedResponses = request.responses.map { response ->
                if (response.providerId == providerId) {
                    response.copy(status = "rejected")
                } else {
                    response
                }
            }

            requestRef.update(
                mapOf(
                    "responses" to updatedResponses.map { it.toMap() },
                    "updatedAt" to System.currentTimeMillis()
                )
            ).await()

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
                .update(
                    mapOf(
                        "status" to "completed",
                        "updatedAt" to System.currentTimeMillis()
                    )
                )
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

    /**
     * Filtrar solicitudes por tipo de servicio
     */
    fun getRequestsByServiceType(serviceType: String): Flow<List<ServiceRequest>> = callbackFlow {
        val listener = firestore.collection(FirebaseConstants.SERVICE_REQUESTS_COLLECTION)
            .whereEqualTo("serviceType", serviceType)
            .whereEqualTo("status", "pending")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val requests = snapshot?.documents?.mapNotNull { doc ->
                    ServiceRequest.fromMap(doc.data)
                } ?: emptyList()

                trySend(requests)
            }

        awaitClose { listener.remove() }
    }
}