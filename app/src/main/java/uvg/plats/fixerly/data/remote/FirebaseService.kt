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
 */

class FirebaseService : ApiService {

    // ----------------------------
    // INSTANCIAS ÚNICAS
    // ----------------------------

    // con by lazy se crean objetos 1 sola vez para luego reutilizar

    // trae la instancia de la autenticación y la guarda aquí
    private val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    // trae la instancia de Firestore y le activa la persistencia (cache local) usando las settings del Builder

     private val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Cache local
                .build()
        }
    }

    // trae el storage de firebase (sirve pa guardar archivos) y la guarda aquí

    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }


    // estas son las colecciones
    // 1 usuarios (general) y otra de requests de usuarios

    private val usersRef = db.collection("users")
    private val requestsRef = db.collection("serviceRequests")

    // ----------------------------
    // MÉTODOS UTILITARIOS
    // ----------------------------


    // funciones de autenticación y de control de usuario 

    fun isUserAuthenticated(): Boolean = auth.currentUser != null
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    fun getCurrentUserEmail(): String? = auth.currentUser?.email

    // ----------------------------
    // USER / PROVIDER
    // ----------------------------

    // sobreescribe la fun y luego digamos que jala la captura del documento en Firestore y la transforma en un objeto user
    override suspend fun getUser(userId: String): User? {
        val snap = usersRef.document(userId).get().await()
        return snap.toObject(User::class.java)
    }

    // se crea el usuario
    override suspend fun createUser(user: User) {
        usersRef.document(user.userId).set(user.toMap()).await()
    }

    // se acutaliza el usuario
    override suspend fun updateUser(userId: String, updates: Map<String, Any>) {
        usersRef.document(userId).update(updates).await()
    }

    // Rating del usuario (YA NO SE USÓ)
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

    // para crear una request 
    override suspend fun createServiceRequest(request: ServiceRequest) {
        requestsRef.document(request.requestId).set(request.toMap()).await()
    }

    // para obtener las requests del cliente y mostrarselas
    override suspend fun getRequestsByClient(clientId: String): List<ServiceRequest> {
        val snap = requestsRef.whereEqualTo("clientId", clientId).get().await()
        return snap.documents.mapNotNull { ServiceRequest.fromMap(it.data) }
    }

    // para obtener las requests de los clientes y mostrarlas a proveedores
    override suspend fun getRequestsByProvider(providerId: String): List<ServiceRequest> {
        val snap = requestsRef
            .whereArrayContains("responses.providerId", providerId)
            .get()
            .await()

        return snap.documents.mapNotNull { ServiceRequest.fromMap(it.data) }
    }

    // para actualizar la requests del usuario
    override suspend fun updateServiceRequest(
        requestId: String,
        updates: Map<String, Any>
    ) {
        requestsRef.document(requestId).update(updates).await()
    }

    // ----------------------------
    // STORAGE
    // ----------------------------

    // para subir imagenes al storage dentro de firebase
    override suspend fun uploadImage(path: String, fileUri: Uri): String {
        val ref = storage.reference.child(path)
        ref.putFile(fileUri).await()
        return ref.downloadUrl.await().toString()
    }
}
