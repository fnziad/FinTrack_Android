package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.SavingsGoalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavingsGoalDao {
    @Query("SELECT * FROM savings_goals ORDER BY id DESC")
    fun getAllSavingsGoals(): Flow<List<SavingsGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingsGoal(goal: SavingsGoalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(goals: List<SavingsGoalEntity>)

    @Update
    suspend fun updateSavingsGoal(goal: SavingsGoalEntity)

    @Query("DELETE FROM savings_goals WHERE id = :id")
    suspend fun deleteSavingsGoalById(id: Int)

    @Query("DELETE FROM savings_goals")
    suspend fun deleteAll()
}
