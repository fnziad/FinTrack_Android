package com.example.shared.`data`.database

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.shared.`data`.dao.LoanDao
import com.example.shared.`data`.dao.LoanDao_Impl
import com.example.shared.`data`.dao.SavingsGoalDao
import com.example.shared.`data`.dao.SavingsGoalDao_Impl
import com.example.shared.`data`.dao.TaskDao
import com.example.shared.`data`.dao.TaskDao_Impl
import com.example.shared.`data`.dao.TransactionDao
import com.example.shared.`data`.dao.TransactionDao_Impl
import com.example.shared.`data`.dao.UserSettingsDao
import com.example.shared.`data`.dao.UserSettingsDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class AppDatabase_Impl : AppDatabase() {
  private val _transactionDao: Lazy<TransactionDao> = lazy {
    TransactionDao_Impl(this)
  }

  private val _savingsGoalDao: Lazy<SavingsGoalDao> = lazy {
    SavingsGoalDao_Impl(this)
  }

  private val _loanDao: Lazy<LoanDao> = lazy {
    LoanDao_Impl(this)
  }

  private val _userSettingsDao: Lazy<UserSettingsDao> = lazy {
    UserSettingsDao_Impl(this)
  }

  private val _taskDao: Lazy<TaskDao> = lazy {
    TaskDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(4,
        "dda4979edf39d4b548f4fac48e9b9203", "4c951a16769e17d1e4640b34c3e21816") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `transactions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT NOT NULL, `amount` REAL NOT NULL, `category` TEXT NOT NULL, `subCategory` TEXT NOT NULL, `description` TEXT NOT NULL, `dateEpochMillis` INTEGER NOT NULL, `dayName` TEXT NOT NULL, `isRecurring` INTEGER NOT NULL, `recurringFrequency` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `savings_goals` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `targetAmount` REAL NOT NULL, `currentAmount` REAL NOT NULL, `targetDateEpochMillis` INTEGER NOT NULL, `category` TEXT NOT NULL, `note` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `loans` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `personName` TEXT NOT NULL, `amount` REAL NOT NULL, `paidAmount` REAL NOT NULL, `loanType` TEXT NOT NULL, `direction` TEXT NOT NULL, `dueDateEpochMillis` INTEGER NOT NULL, `isSettled` INTEGER NOT NULL, `note` TEXT NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `user_settings` (`id` INTEGER NOT NULL, `profileType` TEXT NOT NULL, `userName` TEXT NOT NULL, `initialCash` REAL NOT NULL, `salaryDay` INTEGER NOT NULL, `currencySymbol` TEXT NOT NULL, `targetSavings` REAL NOT NULL, `targetBudget` REAL NOT NULL, `incomeFrequency` TEXT NOT NULL, `colorTheme` TEXT NOT NULL, `isDarkMode` INTEGER NOT NULL, `isDataLoaded` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `financial_tasks` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT NOT NULL, `category` TEXT NOT NULL, `dueDate` TEXT NOT NULL, `priority` TEXT NOT NULL, `isCompleted` INTEGER NOT NULL, `createdAtEpochMillis` INTEGER NOT NULL)")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'dda4979edf39d4b548f4fac48e9b9203')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `transactions`")
        connection.execSQL("DROP TABLE IF EXISTS `savings_goals`")
        connection.execSQL("DROP TABLE IF EXISTS `loans`")
        connection.execSQL("DROP TABLE IF EXISTS `user_settings`")
        connection.execSQL("DROP TABLE IF EXISTS `financial_tasks`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsTransactions: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTransactions.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("type", TableInfo.Column("type", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("amount", TableInfo.Column("amount", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("subCategory", TableInfo.Column("subCategory", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("description", TableInfo.Column("description", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("dateEpochMillis", TableInfo.Column("dateEpochMillis", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("dayName", TableInfo.Column("dayName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("isRecurring", TableInfo.Column("isRecurring", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsTransactions.put("recurringFrequency", TableInfo.Column("recurringFrequency",
            "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTransactions: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTransactions: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTransactions: TableInfo = TableInfo("transactions", _columnsTransactions,
            _foreignKeysTransactions, _indicesTransactions)
        val _existingTransactions: TableInfo = read(connection, "transactions")
        if (!_infoTransactions.equals(_existingTransactions)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |transactions(com.example.shared.data.model.TransactionEntity).
              | Expected:
              |""".trimMargin() + _infoTransactions + """
              |
              | Found:
              |""".trimMargin() + _existingTransactions)
        }
        val _columnsSavingsGoals: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSavingsGoals.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSavingsGoals.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSavingsGoals.put("targetAmount", TableInfo.Column("targetAmount", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSavingsGoals.put("currentAmount", TableInfo.Column("currentAmount", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSavingsGoals.put("targetDateEpochMillis", TableInfo.Column("targetDateEpochMillis",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsSavingsGoals.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSavingsGoals.put("note", TableInfo.Column("note", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSavingsGoals: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSavingsGoals: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSavingsGoals: TableInfo = TableInfo("savings_goals", _columnsSavingsGoals,
            _foreignKeysSavingsGoals, _indicesSavingsGoals)
        val _existingSavingsGoals: TableInfo = read(connection, "savings_goals")
        if (!_infoSavingsGoals.equals(_existingSavingsGoals)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |savings_goals(com.example.shared.data.model.SavingsGoalEntity).
              | Expected:
              |""".trimMargin() + _infoSavingsGoals + """
              |
              | Found:
              |""".trimMargin() + _existingSavingsGoals)
        }
        val _columnsLoans: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsLoans.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("personName", TableInfo.Column("personName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("amount", TableInfo.Column("amount", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("paidAmount", TableInfo.Column("paidAmount", "REAL", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("loanType", TableInfo.Column("loanType", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("direction", TableInfo.Column("direction", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("dueDateEpochMillis", TableInfo.Column("dueDateEpochMillis", "INTEGER",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("isSettled", TableInfo.Column("isSettled", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsLoans.put("note", TableInfo.Column("note", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysLoans: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesLoans: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoLoans: TableInfo = TableInfo("loans", _columnsLoans, _foreignKeysLoans,
            _indicesLoans)
        val _existingLoans: TableInfo = read(connection, "loans")
        if (!_infoLoans.equals(_existingLoans)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |loans(com.example.shared.data.model.LoanEntity).
              | Expected:
              |""".trimMargin() + _infoLoans + """
              |
              | Found:
              |""".trimMargin() + _existingLoans)
        }
        val _columnsUserSettings: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsUserSettings.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("profileType", TableInfo.Column("profileType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("userName", TableInfo.Column("userName", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("initialCash", TableInfo.Column("initialCash", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("salaryDay", TableInfo.Column("salaryDay", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("currencySymbol", TableInfo.Column("currencySymbol", "TEXT", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("targetSavings", TableInfo.Column("targetSavings", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("targetBudget", TableInfo.Column("targetBudget", "REAL", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("incomeFrequency", TableInfo.Column("incomeFrequency", "TEXT",
            true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("colorTheme", TableInfo.Column("colorTheme", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("isDarkMode", TableInfo.Column("isDarkMode", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsUserSettings.put("isDataLoaded", TableInfo.Column("isDataLoaded", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysUserSettings: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesUserSettings: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoUserSettings: TableInfo = TableInfo("user_settings", _columnsUserSettings,
            _foreignKeysUserSettings, _indicesUserSettings)
        val _existingUserSettings: TableInfo = read(connection, "user_settings")
        if (!_infoUserSettings.equals(_existingUserSettings)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |user_settings(com.example.shared.data.model.UserSettingsEntity).
              | Expected:
              |""".trimMargin() + _infoUserSettings + """
              |
              | Found:
              |""".trimMargin() + _existingUserSettings)
        }
        val _columnsFinancialTasks: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsFinancialTasks.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFinancialTasks.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFinancialTasks.put("category", TableInfo.Column("category", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFinancialTasks.put("dueDate", TableInfo.Column("dueDate", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFinancialTasks.put("priority", TableInfo.Column("priority", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsFinancialTasks.put("isCompleted", TableInfo.Column("isCompleted", "INTEGER", true,
            0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsFinancialTasks.put("createdAtEpochMillis", TableInfo.Column("createdAtEpochMillis",
            "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysFinancialTasks: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesFinancialTasks: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoFinancialTasks: TableInfo = TableInfo("financial_tasks", _columnsFinancialTasks,
            _foreignKeysFinancialTasks, _indicesFinancialTasks)
        val _existingFinancialTasks: TableInfo = read(connection, "financial_tasks")
        if (!_infoFinancialTasks.equals(_existingFinancialTasks)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |financial_tasks(com.example.shared.data.model.TaskEntity).
              | Expected:
              |""".trimMargin() + _infoFinancialTasks + """
              |
              | Found:
              |""".trimMargin() + _existingFinancialTasks)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "transactions", "savings_goals",
        "loans", "user_settings", "financial_tasks")
  }

  public override fun clearAllTables() {
    super.performClear(false, "transactions", "savings_goals", "loans", "user_settings",
        "financial_tasks")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(TransactionDao::class, TransactionDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SavingsGoalDao::class, SavingsGoalDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(LoanDao::class, LoanDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(UserSettingsDao::class, UserSettingsDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TaskDao::class, TaskDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun transactionDao(): TransactionDao = _transactionDao.value

  public override fun savingsGoalDao(): SavingsGoalDao = _savingsGoalDao.value

  public override fun loanDao(): LoanDao = _loanDao.value

  public override fun userSettingsDao(): UserSettingsDao = _userSettingsDao.value

  public override fun taskDao(): TaskDao = _taskDao.value
}
