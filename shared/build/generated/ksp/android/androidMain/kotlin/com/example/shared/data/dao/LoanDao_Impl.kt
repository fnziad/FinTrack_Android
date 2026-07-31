package com.example.shared.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shared.`data`.model.LoanEntity
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
public class LoanDao_Impl(
  __db: RoomDatabase,
) : LoanDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfLoanEntity: EntityInsertAdapter<LoanEntity>

  private val __updateAdapterOfLoanEntity: EntityDeleteOrUpdateAdapter<LoanEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfLoanEntity = object : EntityInsertAdapter<LoanEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `loans` (`id`,`title`,`personName`,`amount`,`paidAmount`,`loanType`,`direction`,`dueDateEpochMillis`,`isSettled`,`note`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: LoanEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.personName)
        statement.bindDouble(4, entity.amount)
        statement.bindDouble(5, entity.paidAmount)
        statement.bindText(6, entity.loanType)
        statement.bindText(7, entity.direction)
        statement.bindLong(8, entity.dueDateEpochMillis)
        val _tmp: Int = if (entity.isSettled) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        statement.bindText(10, entity.note)
      }
    }
    this.__updateAdapterOfLoanEntity = object : EntityDeleteOrUpdateAdapter<LoanEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `loans` SET `id` = ?,`title` = ?,`personName` = ?,`amount` = ?,`paidAmount` = ?,`loanType` = ?,`direction` = ?,`dueDateEpochMillis` = ?,`isSettled` = ?,`note` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: LoanEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        statement.bindText(3, entity.personName)
        statement.bindDouble(4, entity.amount)
        statement.bindDouble(5, entity.paidAmount)
        statement.bindText(6, entity.loanType)
        statement.bindText(7, entity.direction)
        statement.bindLong(8, entity.dueDateEpochMillis)
        val _tmp: Int = if (entity.isSettled) 1 else 0
        statement.bindLong(9, _tmp.toLong())
        statement.bindText(10, entity.note)
        statement.bindLong(11, entity.id.toLong())
      }
    }
  }

  public override suspend fun insertLoan(loan: LoanEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __insertAdapterOfLoanEntity.insert(_connection, loan)
  }

  public override suspend fun insertAll(loans: List<LoanEntity>): Unit = performSuspending(__db,
      false, true) { _connection ->
    __insertAdapterOfLoanEntity.insert(_connection, loans)
  }

  public override suspend fun updateLoan(loan: LoanEntity): Unit = performSuspending(__db, false,
      true) { _connection ->
    __updateAdapterOfLoanEntity.handle(_connection, loan)
  }

  public override fun getAllLoans(): Flow<List<LoanEntity>> {
    val _sql: String = "SELECT * FROM loans ORDER BY isSettled ASC, dueDateEpochMillis ASC"
    return createFlow(__db, false, arrayOf("loans")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfPersonName: Int = getColumnIndexOrThrow(_stmt, "personName")
        val _columnIndexOfAmount: Int = getColumnIndexOrThrow(_stmt, "amount")
        val _columnIndexOfPaidAmount: Int = getColumnIndexOrThrow(_stmt, "paidAmount")
        val _columnIndexOfLoanType: Int = getColumnIndexOrThrow(_stmt, "loanType")
        val _columnIndexOfDirection: Int = getColumnIndexOrThrow(_stmt, "direction")
        val _columnIndexOfDueDateEpochMillis: Int = getColumnIndexOrThrow(_stmt,
            "dueDateEpochMillis")
        val _columnIndexOfIsSettled: Int = getColumnIndexOrThrow(_stmt, "isSettled")
        val _columnIndexOfNote: Int = getColumnIndexOrThrow(_stmt, "note")
        val _result: MutableList<LoanEntity> = mutableListOf()
        while (_stmt.step()) {
          val _item: LoanEntity
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpPersonName: String
          _tmpPersonName = _stmt.getText(_columnIndexOfPersonName)
          val _tmpAmount: Double
          _tmpAmount = _stmt.getDouble(_columnIndexOfAmount)
          val _tmpPaidAmount: Double
          _tmpPaidAmount = _stmt.getDouble(_columnIndexOfPaidAmount)
          val _tmpLoanType: String
          _tmpLoanType = _stmt.getText(_columnIndexOfLoanType)
          val _tmpDirection: String
          _tmpDirection = _stmt.getText(_columnIndexOfDirection)
          val _tmpDueDateEpochMillis: Long
          _tmpDueDateEpochMillis = _stmt.getLong(_columnIndexOfDueDateEpochMillis)
          val _tmpIsSettled: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsSettled).toInt()
          _tmpIsSettled = _tmp != 0
          val _tmpNote: String
          _tmpNote = _stmt.getText(_columnIndexOfNote)
          _item =
              LoanEntity(_tmpId,_tmpTitle,_tmpPersonName,_tmpAmount,_tmpPaidAmount,_tmpLoanType,_tmpDirection,_tmpDueDateEpochMillis,_tmpIsSettled,_tmpNote)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun deleteLoanById(id: Int) {
    val _sql: String = "DELETE FROM loans WHERE id = ?"
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
    val _sql: String = "DELETE FROM loans"
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
