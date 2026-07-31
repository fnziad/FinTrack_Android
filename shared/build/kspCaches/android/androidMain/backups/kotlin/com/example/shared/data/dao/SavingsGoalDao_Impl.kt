package com.example.shared.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shared.`data`.model.SavingsGoalEntity
import javax.`annotation`.processing.Generated
import kotlin.Double
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class SavingsGoalDao_Impl(
  __db: RoomDatabase,
) : SavingsGoalDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfSavingsGoalEntity: EntityInsertAdapter<SavingsGoalEntity>

  private val __updateAdapterOfSavingsGoalEntity: EntityDeleteOrUpdateAdapter<SavingsGoalEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfSavingsGoalEntity = object : EntityInsertAdapter<SavingsGoalEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `savings_goals` (`id`,`title`,`targetAmount`,`currentAmount`,`targetDateEpochMillis`,`category`,`note`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: SavingsGoalEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindDouble(3, entity.targetAmount)
        statement.bindDouble(4, entity.currentAmount)
        statement.bindLong(5, entity.targetDateEpochMillis)
        statement.bindText(6, entity.category)
        statement.bindText(7, entity.note)
      }
    }
    this.__updateAdapterOfSavingsGoalEntity = object :
        EntityDeleteOrUpdateAdapter<SavingsGoalEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `savings_goals` SET `id` = ?,`title` = ?,`targetAmount` = ?,`currentAmount` = ?,`targetDateEpochMillis` = ?,`category` = ?,`note` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: SavingsGoalEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindDouble(3, entity.targetAmount)
        statement.bindDouble(4, entity.currentAmount)
        statement.bindLong(5, entity.targetDateEpochMillis)
        statement.bindText(6, entity.category)
        statement.bindText(7, entity.note)
        statement.bindLong(8, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertSavingsGoal(goal: SavingsGoalEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSavingsGoalEntity.insert(_connection, goal)
  }

  public override suspend fun insertAll(goals: List<SavingsGoalEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfSavingsGoalEntity.insert(_connection, goals)
  }

  public override suspend fun updateSavingsGoal(goal: SavingsGoalEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfSavingsGoalEntity.handle(_connection, goal)
  }

  public override fun getAllSavingsGoals(): Flow<List<SavingsGoalEntity>> {
    val _sql: String = "SELECT * FROM savings_goals ORDER BY id DESC"
    return createFlow(__db, false, arrayOf("savings_goals")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfTargetAmount: Int = getColumnIndexOrThrow(_stmt, "targetAmount")
        val _columnIndexOfCurrentAmount: Int = getColumnIndexOrThrow(_stmt, "currentAmount")
        val _columnIndexOfTargetDateEpochMillis: Int = getColumnIndexOrThrow(_stmt,
            "targetDateEpochMillis")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _result: MutableList<SavingsGoalEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: SavingsGoalEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpTargetAmount: Double
          _tmpTargetAmount = _stmt.getDouble(_columnIndexOfTargetAmount)
          val _tmpCurrentAmount: Double
          _tmpCurrentAmount = _stmt.getDouble(_columnIndexOfCurrentAmount)
          val _tmpTargetDateEpochMillis: Long
          _tmpTargetDateEpochMillis = _stmt.getLong(_columnIndexOfTargetDateEpochMillis)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpNote: String
          _tmpNote = _stmt.getText(_columnIndexOfNote)
          _item =
              SavingsGoalEntity(_tmpId,_tmpTitle,_tmpTargetAmount,_tmpCurrentAmount,_tmpTargetDateEpochMillis,_tmpCategory,_tmpNote)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteSavingsGoalById(id: Int) {
    val _sql: String = "DELETE FROM savings_goals WHERE id = ?"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteAll() {
    val _sql: String = "DELETE FROM savings_goals"
    return performSuspending(__db, false, true) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        _stmt.step()
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
