package uvg.plats.fixerly.data.model

import uvg.plats.fixerly.data.local.entities.AddressEntity
import java.util.UUID

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val departamento: String,
    val direccion: String,
    val zona: String,
    val indicaciones: String,
    val isDefault: Boolean = false
) {
    fun toEntity(): AddressEntity {
        return AddressEntity(
            id = id,
            userId = userId,
            departamento = departamento,
            direccion = direccion,
            zona = zona,
            indicaciones = indicaciones,
            isDefault = isDefault
        )
    }

    companion object {
        fun fromEntity(entity: AddressEntity): Address {
            return Address(
                id = entity.id,
                userId = entity.userId,
                departamento = entity.departamento,
                direccion = entity.direccion,
                zona = entity.zona,
                indicaciones = entity.indicaciones,
                isDefault = entity.isDefault
            )
        }
    }
}