package uvg.plats.fixerly.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

/**
 * Servicio centralizado para acceso a instancias de Firebase
 * Útil para testing y mantener una única fuente de verdad
 */
object FirebaseService {

    /**
     * Instancia de Firebase Authentication
     */
    val auth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    /**
     * Instancia de Cloud Firestore
     */
    val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance().apply {
            // Configuraciones opcionales de Firestore
            firestoreSettings = com.google.firebase.firestore.FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true) // Cache local
                .build()
        }
    }

    /**
     * Instancia de Firebase Storage
     */
    val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }

    /**
     * Verificar si hay usuario autenticado
     */
    fun isUserAuthenticated(): Boolean {
        return auth.currentUser != null
    }

    /**
     * Obtener ID del usuario actual
     */
    fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    /**
     * Obtener email del usuario actual
     */
    fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }
}