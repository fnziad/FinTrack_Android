package com.example.shared.data.database

/**
 * expect declaration — each platform provides its own actual implementation
 * that gives the correct file path for the SQLite database.
 */
expect fun getDatabaseBuilder(): androidx.room.RoomDatabase.Builder<AppDatabase>
