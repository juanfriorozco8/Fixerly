package uvg.plats.fixerly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.utils.FirebaseConstants

class AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Usuario actualmente autenticado
     */
    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun isUserLoggedIn(): Boolean = getCurrentUser() != null

    /**
     * Login con email y password
     */
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Error al obtener usuario")
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registro de CLIENTE
     */
    suspend fun registerClient(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        address: Address
    ): Result<String> {
        return try {
            // 1. Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al crear usuario")

            // 2. Crear documento en Firestore
            val user = User(
                userId = userId,
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                userType = FirebaseConstants.USER_TYPE_CLIENT,
                address = address,
                profileImageUrl = "" // URL por defecto o vacío
            )

            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .set(user)
                .await()

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Registro de PROVEEDOR
     */
    suspend fun registerProvider(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        contactPreferences: List<String>,
        about: String,
        skills: List<String>
    ): Result<String> {
        return try {
            // 1. Crear usuario en Firebase Auth
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al crear usuario")

            // 2. Crear documento en Firestore
            val user = User(
                userId = userId,
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                userType = FirebaseConstants.USER_TYPE_PROVIDER,
                contactPreferences = contactPreferences,
                about = about,
                skills = skills,
                profileImageUrl = "" // URL por defecto o vacío
            )

            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .set(user)
                .await()

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener datos del usuario actual
     */
    suspend fun getUserData(userId: String): Result<User> {
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
     * Logout
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Recuperar contraseña
     */
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}