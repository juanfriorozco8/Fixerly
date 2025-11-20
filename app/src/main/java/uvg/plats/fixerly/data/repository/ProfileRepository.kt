package uvg.plats.fixerly.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.utils.FirebaseConstants

class ProfileRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    /**
     * Obtener información del usuario
     */

    // funcion suspend para obtener el perfil de Usuario
    // se toma de la coleccion de firebase
    // se valida tambien y se retorna 
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
     * Actualizar información genérica del perfil con un mapa de campos
     */

    // los mapitas son como diccionarios de Python basicamente xd
    // se obtienen los datos del mapita y se devuelven para el perfil del user
    // se valida también 
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

    // para la foto del perfil pero esto quedó muy secundario, no añadimos lo de actualizar eso directamente 
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
    // de la users collection se sacan los datos de los campos de las skills
    // y se devuelven 
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

    // mismo que el anterior pero pa actualizar el about o el sobre mi
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

     // igual que el anterior, pero para el contacto del proveedor y sus preferencias
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

    // -------------------------------------------------------------------------
    // Helpers extra para que el ProfileViewModel compile sin tocarlo
    // -------------------------------------------------------------------------

    // obviamente no queremos tocar cosas de los viewmodels dentro, por eso usamos repos para evitar andar metiendo cosas ahí

    /**
     * Actualizar info básica: nombre, apellidos, teléfono
     */

    // lo que dice arriba
    // se utiliza mapita para obtener los valores y retornarlos
    suspend fun updateBasicInfo(
        userId: String,
        name: String,
        lastName: String,
        phone: String
    ): Result<Unit> {
        val updates = mapOf(
            FirebaseConstants.FIELD_NAME to name,
            FirebaseConstants.FIELD_LAST_NAME to lastName,
            FirebaseConstants.FIELD_PHONE to phone
        )
        return updateProfile(userId, updates)
    }

    /**
     * Actualizar dirección completa del cliente
     */

    // otro mapita para actualizar la direccion del cliente
    suspend fun updateClientAddress(
        userId: String,
        address: Address
    ): Result<Unit> {
        val updates = mapOf(
            FirebaseConstants.FIELD_ADDRESS to address.toMap()
        )
        return updateProfile(userId, updates)
    }

    /**
     * Wrapper con el mismo nombre que usa el ViewModel
     */

    // otro que se encarga de cargar la imagen del user pero pues no se usa  
    suspend fun uploadProfileImage(
        userId: String,
        imageUri: Uri
    ): Result<String> = updateProfileImage(userId, imageUri)

    /**
     * Eliminar foto de perfil
     */

    // otra que no se usa xd
    suspend fun deleteProfileImage(userId: String): Result<Unit> {
        return try {
            val storageRef = storage.reference
                .child("${FirebaseConstants.STORAGE_PROFILE_IMAGES}/$userId.jpg")

            storageRef.delete().await()

            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .update(FirebaseConstants.FIELD_PROFILE_IMAGE_URL, "")
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener proveedores por habilidad
     */
    
    // tampoco se usa, se debió quitar pero fue de las primeras hechas y la deje  
    suspend fun getProvidersBySkill(skill: String): Result<List<User>> {
        return try {
            val snapshot = firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .whereArrayContains(FirebaseConstants.FIELD_SKILLS, skill)
                .get()
                .await()

            val providers = snapshot.documents.mapNotNull { doc ->
                doc.toObject(User::class.java)
            }

            Result.success(providers)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}