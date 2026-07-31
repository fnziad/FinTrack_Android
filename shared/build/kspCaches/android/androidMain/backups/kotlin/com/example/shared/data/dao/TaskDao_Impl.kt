package com.example.shared.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shared.`data`.model.TaskEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class TaskDao_Impl(
  __db: RoomDatabase,
) : TaskDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTaskEntity: EntityInsertAdapter<TaskEntity>

  private val __updateAdapterOfTaskEntity: EntityDeleteOrUpdateAdapter<TaskEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTaskEntity = object : EntityInsertAdapter<TaskEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `financial_tasks` (`id`,`title`,`category`,`dueDate`,`priority`,`isCompleted`,`createdAtEpochMillis`) VALUES (nullif(?, 0),?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.dueDate)
        statement.bindText(5, entity.priority)
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        statement.bindLong(7, entity.createdAtEpochMillis)
      }
    }
    this.__updateAdapterOfTaskEntity = object : EntityDeleteOrUpdateAdapter<TaskEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `financial_tasks` SET `id` = ?,`title` = ?,`category` = ?,`dueDate` = ?,`priority` = ?,`isCompleted` = ?,`createdAtEpochMillis` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TaskEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.category)
        statement.bindText(4, entity.dueDate)
        statement.bindText(5, entity.priority)
        val _tmp: Int = if (entity.isCompleted) 1 else 0
        statement.bindLong(6, _tmp.toLong())
        statement.bindLong(7, entity.createdAtEpochMillis)
        statement.bindLong(8, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertTask(task: TaskEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfTaskEntity.insert(_connection, task)
  }

  public override suspend fun updateTask(task: TaskEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfTaskEntity.handle(_connection, task)
  }

  public override fun getAllTasks(): Flow<List<TaskEntity>> {
    val _sql: String = "SELECT * FROM financial_tasks ORDER BY isCompleted ASC, id DESC"
    return createFlow(__db, false, arrayOf("financial_tasks")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfDueDate: Int = getColumnIndexOrThrow(_stmt, "dueDate")
        val _columnIndexOfPriority: Int = getColumnIndexOrThrow(_stmt, "priority")
        val _columnIndexOfIsCompleted: Int = getColumnIndexOrThrow(_stmt, "isCompleted")
        val _columnIndexOfCreatedAtEpochMillis: Int = getColumnIndexOrThrow(_stmt,
            "createdAtEpochMillis")
        val _result: MutableList<TaskEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TaskEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpDueDate: String
          _tmpDueDate = _stmt.getText(_columnIndexOfDueDate)
          val _tmpPriority: String
          _tmpPriority = _stmt.getText(_columnIndexOfPriority)
          val _tmpIsCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsCompleted).toInt()
          _tmpIsCompleted = _tmp != 0
          val _tmpCreatedAtEpochMillis: Long
          _tmpCreatedAtEpochMillis = _stmt.getLong(_columnIndexOfCreatedAtEpochMillis)
          _item =
              TaskEntity(_tmpId,_tmpTitle,_tmpCategory,_tmpDueDate,_tmpPriority,_tmpIsCompleted,_tmpCreatedAtEpochMillis)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTaskById(id: Int) {
    val _sql: String = "DELETE FROM financial_tasks WHERE id = ?"
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

  public override suspend fun deleteAllTasks() {
    val _sql: String = "DELETE FROM financial_tasks"
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
