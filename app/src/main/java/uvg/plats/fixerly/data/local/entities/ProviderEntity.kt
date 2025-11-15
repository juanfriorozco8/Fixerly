package uvg.plats.fixerly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "providers")
data class ProviderEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val habilidades: String,
    val detalles: String,
    val contactEmail: Boolean,
    val contactTelefono: Boolean,
    val rating: Float = 0f,
    val totalReviews: Int = 0
)