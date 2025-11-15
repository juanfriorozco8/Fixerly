package uvg.plats.fixerly.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val nombre: String,
    val apellidos: String,
    val email: String,
    val telefono: String,
    val accountType: String,
    val passwordHash: String,
    val createdAt: Long = System.currentTimeMillis()
)