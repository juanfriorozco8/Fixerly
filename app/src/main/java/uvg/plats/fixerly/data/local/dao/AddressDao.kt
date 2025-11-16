package uvg.plats.fixerly.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uvg.plats.fixerly.data.local.entities.AddressEntity

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressEntity)

    @Query("SELECT * FROM addresses WHERE userId = :userId")
    fun getAddressesByUserId(userId: String): Flow<List<AddressEntity>>

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1")
    suspend fun getDefaultAddress(userId: String): AddressEntity?

    @Update
    suspend fun updateAddress(address: AddressEntity)

    @Delete
    suspend fun deleteAddress(address: AddressEntity)
}