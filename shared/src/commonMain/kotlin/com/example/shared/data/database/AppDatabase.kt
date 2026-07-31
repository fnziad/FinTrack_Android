package com.example.shared.data.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.shared.data.dao.LoanDao
import com.example.shared.data.dao.SavingsGoalDao
import com.example.shared.data.dao.TaskDao
import com.example.shared.data.dao.TransactionDao
import com.example.shared.data.dao.UserSettingsDao
import com.example.shared.data.model.LoanEntity
import com.example.shared.data.model.SavingsGoalEntity
import com.example.shared.data.model.TaskEntity
import com.example.shared.data.model.TransactionEntity
import com.example.shared.data.model.UserSettingsEntity
import kotlinx.coroutines.Dispatchers

@Database(
    entities = [
        TransactionEntity::class,
        SavingsGoalEntity::class,
        LoanEntity::class,
        UserSettingsEntity::class,
        TaskEntity::class
    ],
    version = 4,
    exportSchema = false
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun savingsGoalDao(): SavingsGoalDao
    abstract fun loanDao(): LoanDao
    abstract fun userSettingsDao(): UserSettingsDao
    abstract fun taskDao(): TaskDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>

/**
 * Platform-neutral builder helper. Each platform provides the builder
 * via an expect/actual pair in DatabaseFactory.kt.
 */
fun buildAppDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.Default)
        .fallbackToDestructiveMigration(dropAllTables = true)
        .build()
}
