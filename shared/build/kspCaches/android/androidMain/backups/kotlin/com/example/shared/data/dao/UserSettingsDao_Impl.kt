package com.example.shared.`data`.dao

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.shared.`data`.model.UserSettingsEntity
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class UserSettingsDao_Impl(
  __db: RoomDatabase,
) : UserSettingsDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfUserSettingsEntity: EntityInsertAdapter<UserSettingsEntity>

  private val __updateAdapterOfUserSettingsEntity: EntityDeleteOrUpdateAdapter<UserSettingsEntity>
  init {
    this.__db = __db
    this.__insertAdapterOfUserSettingsEntity = object : EntityInsertAdapter<UserSettingsEntity>() {
      protected override fun createQuery(): String =
          "INSERT OR REPLACE INTO `user_settings` (`id`,`profileType`,`userName`,`initialCash`,`salaryDay`,`currencySymbol`,`targetSavings`,`targetBudget`,`incomeFrequency`,`colorTheme`,`isDarkMode`,`isDataLoaded`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: UserSettingsEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.profileType)
        statement.bindText(3, entity.userName)
        statement.bindDouble(4, entity.initialCash)
        statement.bindLong(5, entity.salaryDay.toLong())
        statement.bindText(6, entity.currencySymbol)
        statement.bindDouble(7, entity.targetSavings)
        statement.bindDouble(8, entity.targetBudget)
        statement.bindText(9, entity.incomeFrequency)
        statement.bindText(10, entity.colorTheme)
        val _tmp: Int = if (entity.isDarkMode) 1 else 0
        statement.bindLong(11, _tmp.toLong())
        val _tmp_1: Int = if (entity.isDataLoaded) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
      }
    }
    this.__updateAdapterOfUserSettingsEntity = object :
        EntityDeleteOrUpdateAdapter<UserSettingsEntity>() {
      protected override fun createQuery(): String =
          "UPDATE OR ABORT `user_settings` SET `id` = ?,`profileType` = ?,`userName` = ?,`initialCash` = ?,`salaryDay` = ?,`currencySymbol` = ?,`targetSavings` = ?,`targetBudget` = ?,`incomeFrequency` = ?,`colorTheme` = ?,`isDarkMode` = ?,`isDataLoaded` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: UserSettingsEntity) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.profileType)
        statement.bindText(3, entity.userName)
        statement.bindDouble(4, entity.initialCash)
        statement.bindLong(5, entity.salaryDay.toLong())
        statement.bindText(6, entity.currencySymbol)
        statement.bindDouble(7, entity.targetSavings)
        statement.bindDouble(8, entity.targetBudget)
        statement.bindText(9, entity.incomeFrequency)
        statement.bindText(10, entity.colorTheme)
        val _tmp: Int = if (entity.isDarkMode) 1 else 0
        statement.bindLong(11, _tmp.toLong())
        val _tmp_1: Int = if (entity.isDataLoaded) 1 else 0
        statement.bindLong(12, _tmp_1.toLong())
        statement.bindLong(13, entity.id.toLong())
      }
    }
  }

  public override suspend fun saveUserSettings(settings: UserSettingsEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfUserSettingsEntity.insert(_connection, settings)
  }

  public override suspend fun updateUserSettings(settings: UserSettingsEntity): Unit =
      performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfUserSettingsEntity.handle(_connection, settings)
  }

  public override fun getUserSettings(): Flow<UserSettingsEntity?> {
    val _sql: String = "SELECT * FROM user_settings WHERE id = 1 LIMIT 1"
    return createFlow(__db, false, arrayOf("user_settings")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfProfileType: Int = getColumnIndexOrThrow(_stmt, "profileType")
        val _columnIndexOfUserName: Int = getColumnIndexOrThrow(_stmt, "userName")
        val _columnIndexOfInitialCash: Int = getColumnIndexOrThrow(_stmt, "initialCash")
        val _columnIndexOfSalaryDay: Int = getColumnIndexOrThrow(_stmt, "salaryDay")
        val _columnIndexOfCurrencySymbol: Int = getColumnIndexOrThrow(_stmt, "currencySymbol")
        val _columnIndexOfTargetSavings: Int = getColumnIndexOrThrow(_stmt, "targetSavings")
        val _columnIndexOfTargetBudget: Int = getColumnIndexOrThrow(_stmt, "targetBudget")
        val _columnIndexOfIncomeFrequency: Int = getColumnIndexOrThrow(_stmt, "incomeFrequency")
        val _columnIndexOfColorTheme: Int = getColumnIndexOrThrow(_stmt, "colorTheme")
        val _columnIndexOfIsDarkMode: Int = getColumnIndexOrThrow(_stmt, "isDarkMode")
        val _columnIndexOfIsDataLoaded: Int = getColumnIndexOrThrow(_stmt, "isDataLoaded")
        val _result: UserSettingsEntity?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpProfileType: String
          _tmpProfileType = _stmt.getText(_columnIndexOfProfileType)
          val _tmpUserName: String
          _tmpUserName = _stmt.getText(_columnIndexOfUserName)
          val _tmpInitialCash: Double
          _tmpInitialCash = _stmt.getDouble(_columnIndexOfInitialCash)
          val _tmpSalaryDay: Int
          _tmpSalaryDay = _stmt.getLong(_columnIndexOfSalaryDay).toInt()
          val _tmpCurrencySymbol: String
          _tmpCurrencySymbol = _stmt.getText(_columnIndexOfCurrencySymbol)
          val _tmpTargetSavings: Double
          _tmpTargetSavings = _stmt.getDouble(_columnIndexOfTargetSavings)
          val _tmpTargetBudget: Double
          _tmpTargetBudget = _stmt.getDouble(_columnIndexOfTargetBudget)
          val _tmpIncomeFrequency: String
          _tmpIncomeFrequency = _stmt.getText(_columnIndexOfIncomeFrequency)
          val _tmpColorTheme: String
          _tmpColorTheme = _stmt.getText(_columnIndexOfColorTheme)
          val _tmpIsDarkMode: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfIsDarkMode).toInt()
          _tmpIsDarkMode = _tmp != 0
          val _tmpIsDataLoaded: Boolean
          val _tmp_1: Int
          _tmp_1 = _stmt.getLong(_columnIndexOfIsDataLoaded).toInt()
          _tmpIsDataLoaded = _tmp_1 != 0
          _result =
              UserSettingsEntity(_tmpId,_tmpProfileType,_tmpUserName,_tmpInitialCash,_tmpSalaryDay,_tmpCurrencySymbol,_tmpTargetSavings,_tmpTargetBudget,_tmpIncomeFrequency,_tmpColorTheme,_tmpIsDarkMode,_tmpIsDataLoaded)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
