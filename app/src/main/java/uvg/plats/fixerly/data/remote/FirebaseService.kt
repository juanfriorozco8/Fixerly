package uvg.plats.fixerly.data.remote

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.*

/**
 * FirebaseService:
 * - Provee instancias de Firebase (Auth, Firestore, Storage)
 * - Implementa toda la lógica remota por medio de ApiService
 * - Mantiene TODO centralizado en un solo archivo
 */
class FirebaseService : ApiService {

    // ----------------------------
    // INSTANCIAS ÚNICAS
    // ----------------------------
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Cache local
                .build()
        }
    }

    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    private val usersRef = db.collection("users")
    private val requestsRef = db.collection("serviceRequests")

    // ----------------------------
    // MÉTODOS UTILITARIOS
    // ----------------------------

    fun isUserAuthenticated(): Boolean = auth.currentUser != null
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    // ----------------------------
    // USER / PROVIDER
    // ----------------------------
    override suspend fun getUser(userId: String): User? {
        val snap = usersRef.document(userId).get().await()
        return snap.toObject(User::class.java)
    }

    override suspend fun createUser(user: User) {
        usersRef.document(user.userId).set(user.toMap()).await()
    }

    override suspend fun updateUser(userId: String, updates: Map<String, Any>) {
        usersRef.document(userId).update(updates).await()
    }

    override suspend fun updateProviderRating(providerId: String, newRating: Double) {
        val snap = usersRef.document(providerId).get().await()
        val provider = snap.toObject(User::class.java) ?: return

        val newCount = provider.ratingCount + 1
        val newAverage = ((provider.rating * provider.ratingCount) + newRating) / newCount

        usersRef.document(providerId).update(
            mapOf(
                "rating" to newAverage,
                "ratingCount" to newCount
            )
        ).await()
    }

    // ----------------------------
    // SERVICE REQUESTS
    // ----------------------------
    override suspend fun createServiceRequest(request: ServiceRequest) {
        requestsRef.document(request.requestId).set(request.toMap()).await()
    }

    override suspend fun getRequestsByClient(clientId: String): List<ServiceRequest> {
        val snap = requestsRef.whereEqualTo("clientId", clientId).get().await()
        return snap.documents.mapNotNull { ServiceRequest.fromMap(it.data) }
    }

    override suspend fun getRequestsByProvider(providerId: String): List<ServiceRequest> {
        val snap = requestsRef
            .whereArrayContains("responses.providerId", providerId)
            .get()
            .await()

        return snap.documents.mapNotNull { ServiceRequest.fromMap(it.data) }
    }

    override suspend fun updateServiceRequest(
        requestId: String,
        updates: Map<String, Any>
    ) {
        requestsRef.document(requestId).update(updates).await()
    }

    // ----------------------------
    // STORAGE
    // ----------------------------
    override suspend fun uploadImage(path: String, fileUri: Uri): String {
        val ref = storage.reference.child(path)
        ref.putFile(fileUri).await()
        return ref.downloadUrl.await().toString()
    }
}
