package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.LoanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LoanDao {
    @Query("SELECT * FROM loans ORDER BY isSettled ASC, dueDateEpochMillis ASC")
    fun getAllLoans(): Flow<List<LoanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoan(loan: LoanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(loans: List<LoanEntity>)

    @Update
    suspend fun updateLoan(loan: LoanEntity)

    @Query("DELETE FROM loans WHERE id = :id")
    suspend fun deleteLoanById(id: Int)

    @Query("DELETE FROM loans")
    suspend fun deleteAll()
}
