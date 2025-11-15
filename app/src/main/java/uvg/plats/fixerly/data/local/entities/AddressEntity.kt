package uvg.plats.fixerly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class AddressEntity(
    @PrimaryKey
    val id: String,
    val userId: String,
    val departamento: String,
    val direccion: String,
    val zona: String,
    val indicaciones: String,
    val isDefault: Boolean = false
)