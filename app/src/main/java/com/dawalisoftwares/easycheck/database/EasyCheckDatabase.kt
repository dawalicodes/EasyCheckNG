package com.dawalisoftwares.easycheck.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dawalisoftwares.easycheck.INFO
import com.dawalisoftwares.easycheck.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [ItemEntity::class, QuickActionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class EasyCheckDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun quickActionDao(): QuickActionDao

    companion object {
        @Volatile
        private var INSTANCE: EasyCheckDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `${INFO.TABLE_NAME_QUICK_ACTION}` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `itemName` TEXT NOT NULL,
                        `position` INTEGER NOT NULL,
                        FOREIGN KEY(`itemName`) REFERENCES `${INFO.TABLE_NAME_EASY_CHECK_ITEM}`(`name`) ON UPDATE CASCADE ON DELETE CASCADE
                    )
                    """.trimIndent()
                )
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_quick_actions_itemName` ON `${INFO.TABLE_NAME_QUICK_ACTION}` (`itemName`)")
                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_quick_actions_position` ON `${INFO.TABLE_NAME_QUICK_ACTION}` (`position`)")

                db.execSQL("INSERT OR IGNORE INTO `${INFO.TABLE_NAME_QUICK_ACTION}` (`itemName`, `position`) VALUES ('${INFO.CHECK_AIRTIME_BALANCE}', 1)")
                db.execSQL("INSERT OR IGNORE INTO `${INFO.TABLE_NAME_QUICK_ACTION}` (`itemName`, `position`) VALUES ('${INFO.CHECK_DATA_BALANCE}', 2)")
                db.execSQL("INSERT OR IGNORE INTO `${INFO.TABLE_NAME_QUICK_ACTION}` (`itemName`, `position`) VALUES ('${INFO.CHECK_PHONE_NUMBER}', 3)")
                db.execSQL("INSERT OR IGNORE INTO `${INFO.TABLE_NAME_QUICK_ACTION}` (`itemName`, `position`) VALUES ('${INFO.CHECK_NIN}', 4)")
            }
        }

        fun getDatabase(context: Context): EasyCheckDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EasyCheckDatabase::class.java,
                    "easycheck_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration(true)
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // onCreate runs ONLY once when the database file is first created
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialItems(database.itemDao(), database.quickActionDao())
                    }
                }
            }

            private suspend fun populateInitialItems(itemDao: ItemDao, quickActionDao: QuickActionDao) {
                val initialItems = listOf(
                    ItemEntity(INFO.CHECK_AIRTIME_BALANCE, "Checks your account balance.", R.drawable.baseline_account_balance_wallet_24, "#FEF3C7", "#F59E0B", 18, false),
                    ItemEntity(INFO.CHECK_DATA_BALANCE, "Checks your data bundle balance", R.drawable.baseline_wifi_24, "#E0F2FE", "#1683E8", 17, false),
                    ItemEntity(INFO.CHECK_PHONE_NUMBER, "Checks your phone number", R.drawable.baseline_local_phone_24, "#DCFCE7", "#16A34A", 16, false),
                    ItemEntity(INFO.CHECK_NIN, "Retrieve or link your national identification number", R.drawable.id_card_2, "#DBEAFE", "#2563EB", 15, false),
                    ItemEntity(INFO.CHECK_TARIFF_PLAN, "See what tariff plan you're subscribed to", R.drawable.sell, "#EDE9FE", "#6D28D9", 14, false),
                    ItemEntity(INFO.PRIVATE_NUMBER, "Stay anonymous by hiding your number", R.drawable.visibility_off, "#DCFCE7", "#16A34A", 13, false),
                    ItemEntity(INFO.NETWORK_LOOKUP, "checks a phone number network", R.drawable.cell_tower, "#EDE9FE", "#6D28D9", 12, false),
                    ItemEntity(INFO.LOAD_AIRTIME, "Load airtime[recharge card]", R.drawable.credit_card, "#EDE9FE", "#6D28D9", 11, false),
                    ItemEntity(INFO.LOAD_AIRTIME_FROM_BANK, "Recharge directly from your bank account", R.drawable.account_balance, "#DBEAFE", "#2563EB", 10, false),
                    ItemEntity(INFO.BANK_SERVICE, "perform normal bank operations", R.drawable.location_city, "#DBEAFE", "#2563EB", 9, false),
                    ItemEntity(INFO.BUY_DATA, "Buy a data bundle", R.drawable.shopping_cart, "#DCFCE7", "#16A34A", 8, false),
                    ItemEntity(INFO.BORROW_AIRTIME, "Borrow airtime and pay on your next recharge", R.drawable.money_bag, "#FCE7F3", "#DB2777", 7, false),
                    ItemEntity(INFO.BORROW_DATA, "Borrow data and pay on your next recharge", R.drawable.database, "#FEF3C7", "#D97706", 6, false),
                    ItemEntity(INFO.TRANSFER_AIRTIME, "Transfer airtime to other phone number on your network", R.drawable.send, "#EDE9FE", "#7C3AED", 5, false),
                    ItemEntity(INFO.TRANSFER_DATA, "Transfer data to other phone number on your network", R.drawable.compare_arrows, "#E0F2FE", "#0284C7", 4, false),
                    ItemEntity(INFO.CALL_CUSTOMER_CARE, "Place a call to ISP customer care", R.drawable.support_agent, "#DCFCE7", "#16A34A", 3, false),
                    ItemEntity(INFO.SELF_SERVICE, "Self service.", R.drawable.settings, "#EDE9FE", "#6D28D9", 2, false),
                    ItemEntity(INFO.CANCEL_AUTO_RENEW, "Stop services from automatically renewing.", R.drawable.autorenew, "#FEE2E2", "#DC2626", 1,false)
                )
                itemDao.insertAll(initialItems)

                val initialQuickActions = listOf(
                    QuickActionEntity(itemName = INFO.CHECK_AIRTIME_BALANCE, position = 1),
                    QuickActionEntity(itemName = INFO.CHECK_DATA_BALANCE, position = 2),
                    QuickActionEntity(itemName = INFO.CHECK_PHONE_NUMBER, position = 3),
                    QuickActionEntity(itemName = INFO.CHECK_NIN, position = 4)
                )
                quickActionDao.insertAllQuickActions(initialQuickActions)
            }
        }
    }
}
