package uvg.plats.fixerly.data.remote

import android.net.Uri
import uvg.plats.fixerly.data.model.*

interface ApiService {

    // USER / PROVIDER
    suspend fun getUser(userId: String): User?
    suspend fun createUser(user: User)
    suspend fun updateUser(userId: String, updates: Map<String, Any>)
    suspend fun updateProviderRating(providerId: String, newRating: Double)

    // REQUESTS
    suspend fun createServiceRequest(request: ServiceRequest)
    suspend fun getRequestsByClient(clientId: String): List<ServiceRequest>
    suspend fun getRequestsByProvider(providerId: String): List<ServiceRequest>
    suspend fun updateServiceRequest(requestId: String, updates: Map<String, Any>)

    // STORAGE
    suspend fun uploadImage(path: String, fileUri: Uri): String
}
