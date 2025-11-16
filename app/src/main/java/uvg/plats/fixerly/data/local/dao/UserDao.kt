package uvg.plats.fixerly.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uvg.plats.fixerly.data.local.entities.UserEntity

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash")
    suspend fun login(email: String, passwordHash: String): UserEntity?

    @Update
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE accountType = :type")
    fun getUsersByType(type: String): Flow<List<UserEntity>>
}