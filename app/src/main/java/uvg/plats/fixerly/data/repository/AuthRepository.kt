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

    fun getCurrentUser(): FirebaseUser? = auth.currentUser

    fun isUserLoggedIn(): Boolean = getCurrentUser() != null

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Error al obtener usuario")
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun registerClient(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        address: Address
    ): Result<String> {
        return try {
            android.util.Log.d("AuthRepository", "Creando cuenta de cliente para: $email")
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al crear usuario")

            android.util.Log.d("AuthRepository", "Usuario creado con ID: $userId")

            val user = User(
                userId = userId,
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                userType = FirebaseConstants.TYPE_CLIENT,
                address = address,
                profileImageUrl = ""
            )

            android.util.Log.d("AuthRepository", "Guardando datos del cliente en Firestore")
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .set(user)
                .await()

            android.util.Log.d("AuthRepository", "Cliente registrado exitosamente")
            Result.success(userId)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error al registrar cliente: ${e.message}", e)
            Result.failure(e)
        }
    }

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
            android.util.Log.d("AuthRepository", "Creando cuenta de proveedor para: $email")
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al crear usuario")

            android.util.Log.d("AuthRepository", "Proveedor creado con ID: $userId")

            val user = User(
                userId = userId,
                name = name,
                lastName = lastName,
                email = email,
                phone = phone,
                userType = FirebaseConstants.TYPE_PROVIDER,
                contactPreferences = contactPreferences,
                about = about,
                skills = skills,
                profileImageUrl = ""
            )

            android.util.Log.d("AuthRepository", "Guardando datos del proveedor en Firestore")
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .set(user)
                .await()

            android.util.Log.d("AuthRepository", "Proveedor registrado exitosamente")
            Result.success(userId)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error al registrar proveedor: ${e.message}", e)
            Result.failure(e)
        }
    }

    suspend fun getUserData(userId: String): Result<User> {
        return try {
            val snapshot = firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .get()
                .await()

            if (!snapshot.exists()) {
                return Result.failure(Exception("Usuario no encontrado en Firestore"))
            }

            val user = snapshot.toObject(User::class.java)
                ?: throw Exception("Error al deserializar usuario")

            Result.success(user)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error al obtener datos del usuario: ${e.message}", e)
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
