package com.example.individualproject3

import android.content.Context
import androidx.room.*

// Updated Entity to include a "role" or "isParent" flag
@Entity(tableName = "users") // Renamed to "users" for broader usage
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val email: String,
    val passwordHash: String, // Consider using secure hashing
    val isParent: Boolean // Added to distinguish user roles
)

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) // Allow updates
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash")
    suspend fun getUserByEmailAndPassword(email: String, passwordHash: String): User?

    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): User?
}

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

object Database {
    private lateinit var database: AppDatabase

    fun initialize(context: Context) {
        database = AppDatabase.getDatabase(context)
    }

    // Register user with "isParent" field
    suspend fun registerUser(name: String, email: String, passwordHash: String, isParent: Boolean): Boolean {
        val user = User(0, name, email, passwordHash, isParent)
        return try {
            database.userDao().insertUser(user)
            true
        } catch (e: Exception) {
            false
        }
    }

    // Login user
    suspend fun loginUser(email: String, passwordHash: String): User? {
        return database.userDao().getUserByEmailAndPassword(email, passwordHash)
    }
    suspend fun isEmailRegistered(email: String): Boolean {
        return database.userDao().getUserByEmail(email) != null
    }
}
