package uvg.plats.fixerly.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uvg.plats.fixerly.data.local.dao.*
import uvg.plats.fixerly.data.local.entities.*

@Database(
    entities = [
        UserEntity::class,
        ServiceRequestEntity::class,
        ProviderEntity::class,
        AddressEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class FixerlyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun serviceRequestDao(): ServiceRequestDao
    abstract fun providerDao(): ProviderDao
    abstract fun addressDao(): AddressDao

    companion object {
        @Volatile
        private var INSTANCE: FixerlyDatabase? = null

        fun getDatabase(context: Context): FixerlyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FixerlyDatabase::class.java,
                    "fixerly_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}