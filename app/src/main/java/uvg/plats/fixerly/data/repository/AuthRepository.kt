package uvg.plats.fixerly.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.Address
import uvg.plats.fixerly.data.model.User
import uvg.plats.fixerly.utils.FirebaseConstants

class AuthRepository {

    // se crean los valores que tendran la instancia de firebase y sus servicios
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // las funciones para obtener el usuario y login
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    fun isUserLoggedIn(): Boolean = getCurrentUser() != null


    // función suspend del login, donde se comprueba si está validado el usuario o no
    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: throw Exception("Error al obtener usuario")
            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // funcion suspend del registro, igual, metodos de validacion si está bien o no, además de la comparación con los datos reales dentro de la DB
    // solo para el cliente
    suspend fun registerClient(
        name: String,
        lastName: String,
        email: String,
        password: String,
        phone: String,
        address: Address
    ): Result<String> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al crear usuario")

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

            // CAMBIO AQUÍ: usar .toMap()
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .set(user.toMap())
                .await()

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // funcion suspend del registro, igual, metodos de validacion si está bien o no, además de la comparación con los datos reales dentro de la DB
    // solo para el proveedor

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
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: throw Exception("Error al crear usuario")

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

            // CAMBIO AQUÍ: usar .toMap()
            firestore.collection(FirebaseConstants.USERS_COLLECTION)
                .document(userId)
                .set(user.toMap())
                .await()

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // aqui tenemos la función suspendida para conseguir la data del usuario 
    // se valida y si no existe da una excepción.
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


    // logout 
    fun logout() {
        auth.signOut()
    }


    // restear password (creo que este si no lo tocamos)
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}