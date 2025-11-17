package uvg.plats.fixerly.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.utils.FirebaseConstants

class ProfileRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    /**
     * Obtener información del usuario
     */
    suspend fun getUserProfile(userId: String): Result<User> {
        return try {
            val snapshot = firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            val user = snapshot.toObject(User::class.java)
                ?: throw Exception("Usuario no encontrado")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar información básica del perfil
     */
    suspend fun updateProfile(
        userId: String,
        updates: Map<String, Any>
    ): Result<Unit> {
        return try {
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .update(updates)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar foto de perfil
     */
    suspend fun updateProfileImage(userId: String, imageUri: Uri): Result<String> {
        return try {
            // 1. Subir imagen a Firebase Storage
            val storageRef = storage.reference
                .child("${FirebaseConstants.STORAGE_PROFILE_IMAGES}/$userId.jpg")

            val uploadTask = storageRef.putFile(imageUri).await()

            // 2. Obtener URL de descarga
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()

            // 3. Actualizar URL en Firestore
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .update(FirebaseConstants.FIELD_PROFILE_IMAGE_URL, downloadUrl)
                .await()

            Result.success(downloadUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar habilidades del proveedor
     */
    suspend fun updateProviderSkills(userId: String, skills: List<String>): Result<Unit> {
        return try {
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .update(FirebaseConstants.FIELD_SKILLS, skills)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar descripción del proveedor
     */
    suspend fun updateProviderAbout(userId: String, about: String): Result<Unit> {
        return try {
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .update(FirebaseConstants.FIELD_ABOUT, about)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar preferencias de contacto del proveedor
     */
    suspend fun updateContactPreferences(
        userId: String,
        preferences: List<String>
    ): Result<Unit> {
        return try {
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .update(FirebaseConstants.FIELD_CONTACT_PREFERENCES, preferences)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}