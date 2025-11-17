package uvg.plats.fixerly.data.model

import uvg.plats.fixerly.data.local.entities.AddressEntity
import java.util.UUID

data class Address(
    val id: String = UUID.randomUUID().toString(),
    val userId: String = "",

    val department: String = "",
    val address: String = "",
    val zone: String = "",
    val directions: String = "",

    val isDefault: Boolean = false
) {

    fun isEmpty(): Boolean {
        return department.isBlank() && address.isBlank() && zone.isBlank()
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to id,
            "userId" to userId,
            "department" to department,
            "address" to address,
            "zone" to zone,
            "directions" to directions,
            "isDefault" to isDefault
        )
    }

    fun toEntity(): AddressEntity {
        return AddressEntity(
            id = id,
            userId = userId,
            departamento = department,
            direccion = address,
            zona = zone,
            indicaciones = directions,
            isDefault = isDefault
        )
    }

    companion object {

        fun fromMap(map: Map<String, Any>?): Address? {
            if (map == null) return null

            return Address(
                id = map["id"] as? String ?: UUID.randomUUID().toString(),
                userId = map["userId"] as? String ?: "",
                department = map["department"] as? String ?: "",
                address = map["address"] as? String ?: "",
                zone = map["zone"] as? String ?: "",
                directions = map["directions"] as? String ?: "",
                isDefault = map["isDefault"] as? Boolean ?: false
            )
        }

        fun fromEntity(entity: AddressEntity): Address {
            return Address(
                id = entity.id,
                userId = entity.userId,
                department = entity.departamento,
                address = entity.direccion,
                zone = entity.zona,
                directions = entity.indicaciones,
                isDefault = entity.isDefault
            )
        }
    }
}
