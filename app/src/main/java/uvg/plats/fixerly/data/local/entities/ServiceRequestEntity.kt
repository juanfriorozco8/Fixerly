package uvg.plats.fixerly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "service_requests")
data class ServiceRequestEntity(
    @PrimaryKey
    val id: String,
    val clientId: String,
    val categoria: String,
    val descripcionBreve: String,
    val descripcionDetallada: String,
    val estado: String = "Pendiente",
    val createdAt: Long = System.currentTimeMillis()
)