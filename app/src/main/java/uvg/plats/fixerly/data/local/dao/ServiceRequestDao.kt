package uvg.plats.fixerly.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uvg.plats.fixerly.data.local.entities.ServiceRequestEntity

@Dao
interface ServiceRequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRequest(request: ServiceRequestEntity)

    @Query("SELECT * FROM service_requests WHERE id = :requestId")
    suspend fun getRequestById(requestId: String): ServiceRequestEntity?

    @Query("SELECT * FROM service_requests WHERE clientId = :clientId ORDER BY createdAt DESC")
    fun getRequestsByClient(clientId: String): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests WHERE categoria = :categoria ORDER BY createdAt DESC")
    fun getRequestsByCategory(categoria: String): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests WHERE estado = :estado ORDER BY createdAt DESC")
    fun getRequestsByStatus(estado: String): Flow<List<ServiceRequestEntity>>

    @Query("SELECT * FROM service_requests ORDER BY createdAt DESC")
    fun getAllRequests(): Flow<List<ServiceRequestEntity>>

    @Update
    suspend fun updateRequest(request: ServiceRequestEntity)

    @Delete
    suspend fun deleteRequest(request: ServiceRequestEntity)
}