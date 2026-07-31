package com.example.shared.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shared.`data`.model.TransactionEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
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
public class TransactionDao_Impl(
  __db: RoomDatabase,
) : TransactionDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTransactionEntity: EntityInsertAdapter<TransactionEntity>

  private val __updateAdapterOfTransactionEntity: EntityDeleteOrUpdateAdapter<TransactionEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfTransactionEntity = object : EntityInsertAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `transactions` (`id`,`type`,`amount`,`category`,`subCategory`,`description`,`dateEpochMillis`,`dayName`,`isRecurring`,`recurringFrequency`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.type)
        statement.bindDouble(3, entity.amount)
        statement.bindText(4, entity.category)
        statement.bindText(5, entity.subCategory)
        statement.bindText(6, entity.description)
        statement.bindLong(7, entity.dateEpochMillis)
        statement.bindText(8, entity.dayName)
        val _tmp: Int = if (entity.isRecurring) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        statement.bindText(10, entity.recurringFrequency)
      }
    }
    this.__updateAdapterOfTransactionEntity = object :
        EntityDeleteOrUpdateAdapter<TransactionEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `transactions` SET `id` = ?,`type` = ?,`amount` = ?,`category` = ?,`subCategory` = ?,`description` = ?,`dateEpochMillis` = ?,`dayName` = ?,`isRecurring` = ?,`recurringFrequency` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: TransactionEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.type)
        statement.bindDouble(3, entity.amount)
        statement.bindText(4, entity.category)
        statement.bindText(5, entity.subCategory)
        statement.bindText(6, entity.description)
        statement.bindLong(7, entity.dateEpochMillis)
        statement.bindText(8, entity.dayName)
        val _tmp: Int = if (entity.isRecurring) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        statement.bindText(10, entity.recurringFrequency)
        statement.bindLong(11, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertTransaction(transaction: TransactionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionEntity.insert(_connection, transaction)
  }

  public override suspend fun insertAll(transactions: List<TransactionEntity>): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTransactionEntity.insert(_connection, transactions)
  }

  public override suspend fun updateTransaction(transaction: TransactionEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfTransactionEntity.handle(_connection, transaction)
  }

  public override fun getAllTransactions(): Flow<List<TransactionEntity>> {
    val _sql: String = "SELECT * FROM transactions ORDER BY dateEpochMillis DESC"
    return createFlow(__db, false, arrayOf("transactions")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfType: Int = getColumnIndexOrThrow(_stmt, "type")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfCategory: Int = getColumnIndexOrThrow(_stmt, "category")
        val _columnIndexOfSubCategory: Int = getColumnIndexOrThrow(_stmt, "subCategory")
        val _columnIndexOfDescription: Int = getColumnIndexOrThrow(_stmt, "description")
        val _columnIndexOfDateEpochMillis: Int = getColumnIndexOrThrow(_stmt, "dateEpochMillis")
        val _columnIndexOfDayName: Int = getColumnIndexOrThrow(_stmt, "dayName")
        val _columnIndexOfIsRecurring: Int = getColumnIndexOrThrow(_stmt, "isRecurring")
        val _columnIndexOfRecurringFrequency: Int = getColumnIndexOrThrow(_stmt,
            "recurringFrequency")
        val _result: MutableList<TransactionEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: TransactionEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpType: String
          _tmpType = _stmt.getText(_columnIndexOfType)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpCategory: String
          _tmpCategory = _stmt.getText(_columnIndexOfCategory)
          val _tmpSubCategory: String
          _tmpSubCategory = _stmt.getText(_columnIndexOfSubCategory)
          val _tmpDescription: String
          _tmpDescription = _stmt.getText(_columnIndexOfDescription)
          val _tmpDateEpochMillis: Long
          _tmpDateEpochMillis = _stmt.getLong(_columnIndexOfDateEpochMillis)
          val _tmpDayName: String
          _tmpDayName = _stmt.getText(_columnIndexOfDayName)
          val _tmpIsRecurring: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsRecurring).toInt()
          _tmpIsRecurring = _tmp != 0
          val _tmpRecurringFrequency: String
          _tmpRecurringFrequency = _stmt.getText(_columnIndexOfRecurringFrequency)
          _item =
              TransactionEntity(_tmpId,_tmpType,_tmpAmount,_tmpCategory,_tmpSubCategory,_tmpDescription,_tmpDateEpochMillis,_tmpDayName,_tmpIsRecurring,_tmpRecurringFrequency)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteTransactionById(id: Int) {
    val _sql: String = "DELETE FROM transactions WHERE id = ?"
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

  public override suspend fun deleteAllTransactions() {
    val _sql: String = "DELETE FROM transactions"
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
