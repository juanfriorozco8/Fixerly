package uvg.plats.fixerly.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import uvg.plats.fixerly.data.model.Rating
import java.util.UUID

class RatingRepository {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        private const val RATINGS_COLLECTION = "ratings"
    }

    /**
     * Crear una nueva valoración
     */
    suspend fun createRating(
        clientId: String,
        clientName: String,
        providerId: String,
        providerName: String,
        requestId: String,
        rating: Float,
        comment: String
    ): Result<String> {
        return try {
            val ratingObj = Rating(
                ratingId = UUID.randomUUID().toString(),
                clientId = clientId,
                clientName = clientName,
                providerId = providerId,
                providerName = providerName,
                requestId = requestId,
                rating = rating,
                comment = comment
            )

            firestore.collection(RATINGS_COLLECTION)
                .document(ratingObj.ratingId)
                .set(ratingObj.toMap())
                .await()

            // Actualizar promedio del proveedor
            updateProviderAverageRating(providerId)

            Result.success(ratingObj.ratingId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener valoraciones de un proveedor
     */
    suspend fun getProviderRatings(providerId: String): Result<List<Rating>> {
        return try {
            val snapshot = firestore.collection(RATINGS_COLLECTION)
                .whereEqualTo("providerId", providerId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val ratings = snapshot.documents.mapNotNull { doc ->
                Rating.fromMap(doc.data)
            }

            Result.success(ratings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener promedio de calificación de un proveedor
     */
    suspend fun getProviderAverageRating(providerId: String): Result<Float> {
        return try {
            val ratingsResult = getProviderRatings(providerId)

            ratingsResult.fold(
                onSuccess = { ratings ->
                    if (ratings.isEmpty()) {
                        Result.success(0f)
                    } else {
                        val average = ratings.map { it.rating }.average().toFloat()
                        Result.success(average)
                    }
                },
                onFailure = { error ->
                    Result.failure(error)
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Verificar si un cliente ya valoró una solicitud
     */
    suspend fun hasClientRatedRequest(
        clientId: String,
        requestId: String
    ): Result<Boolean> {
        return try {
            val snapshot = firestore.collection(RATINGS_COLLECTION)
                .whereEqualTo("clientId", clientId)
                .whereEqualTo("requestId", requestId)
                .get()
                .await()

            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Actualizar el promedio de calificación en el perfil del proveedor
     */
    private suspend fun updateProviderAverageRating(providerId: String) {
        try {
            val averageResult = getProviderAverageRating(providerId)

            averageResult.fold(
                onSuccess = { average ->
                    // Obtener también el conteo total
                    val ratingsResult = getProviderRatings(providerId)
                    ratingsResult.fold(
                        onSuccess = { ratings ->
                            val updates = mapOf(
                                "averageRating" to average,
                                "totalRatings" to ratings.size
                            )

                            firestore.collection("users")
                                .document(providerId)
                                .update(updates)
                                .await()
                        },
                        onFailure = { /* Ignorar error */ }
                    )
                },
                onFailure = { /* Ignorar error */ }
            )
        } catch (e: Exception) {
            // Ignorar errores en actualización secundaria
        }
    }

    /**
     * Eliminar una valoración
     */
    suspend fun deleteRating(ratingId: String): Result<Unit> {
        return try {
            // Obtener la valoración antes de eliminarla para actualizar el promedio
            val snapshot = firestore.collection(RATINGS_COLLECTION)
                .document(ratingId)
                .get()
                .await()

            val rating = Rating.fromMap(snapshot.data)

            // Eliminar la valoración
            firestore.collection(RATINGS_COLLECTION)
                .document(ratingId)
                .delete()
                .await()

            // Actualizar promedio si se obtuvo el providerId
            rating?.let {
                updateProviderAverageRating(it.providerId)
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Obtener valoraciones de un cliente
     */
    suspend fun getClientRatings(clientId: String): Result<List<Rating>> {
        return try {
            val snapshot = firestore.collection(RATINGS_COLLECTION)
                .whereEqualTo("clientId", clientId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()

            val ratings = snapshot.documents.mapNotNull { doc ->
                Rating.fromMap(doc.data)
            }

            Result.success(ratings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}