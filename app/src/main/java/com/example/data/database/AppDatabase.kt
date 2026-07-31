package com.example.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.LoanDao
import com.example.data.dao.SavingsGoalDao
import com.example.data.dao.TaskDao
import com.example.data.dao.TransactionDao
import com.example.data.dao.UserSettingsDao
import com.example.data.model.LoanEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TaskEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserSettingsEntity

@Database(
    entities = [
        TransactionEntity::class,
        SavingsGoalEntity::class,
        LoanEntity::class,
        UserSettingsEntity::class,
        TaskEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun loanDao(): LoanDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "takatrack_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
