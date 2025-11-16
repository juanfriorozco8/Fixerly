package uvg.plats.fixerly.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uvg.plats.fixerly.data.local.entities.ProviderEntity

@Dao
interface ProviderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvider(provider: ProviderEntity)

    @Query("SELECT * FROM providers WHERE id = :providerId")
    suspend fun getProviderById(providerId: String): ProviderEntity?

    @Query("SELECT * FROM providers WHERE userId = :userId")
    suspend fun getProviderByUserId(userId: String): ProviderEntity?

    @Query("SELECT * FROM providers WHERE habilidades LIKE '%' || :habilidad || '%'")
    fun getProvidersBySkill(habilidad: String): Flow<List<ProviderEntity>>

    @Query("SELECT * FROM providers ORDER BY rating DESC")
    fun getAllProviders(): Flow<List<ProviderEntity>>

    @Update
    suspend fun updateProvider(provider: ProviderEntity)
}