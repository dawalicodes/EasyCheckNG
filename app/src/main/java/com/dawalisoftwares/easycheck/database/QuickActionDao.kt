package com.dawalisoftwares.easycheck.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dawalisoftwares.easycheck.INFO
import kotlinx.coroutines.flow.Flow

@Dao
interface QuickActionDao {

    @Query(
        "SELECT ${INFO.TABLE_NAME_EASY_CHECK_ITEM}.* " +
        "FROM ${INFO.TABLE_NAME_EASY_CHECK_ITEM} " +
        "INNER JOIN ${INFO.TABLE_NAME_QUICK_ACTION} " +
        "ON ${INFO.TABLE_NAME_EASY_CHECK_ITEM}.name = ${INFO.TABLE_NAME_QUICK_ACTION}.itemName " +
        "ORDER BY ${INFO.TABLE_NAME_QUICK_ACTION}.position ASC"
    )
    fun getQuickActionItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM ${INFO.TABLE_NAME_QUICK_ACTION} ORDER BY position ASC")
    fun getAllQuickActions(): Flow<List<QuickActionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuickAction(quickAction: QuickActionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllQuickActions(quickActions: List<QuickActionEntity>)

    @Update
    suspend fun updateQuickAction(quickAction: QuickActionEntity)

    @Delete
    suspend fun deleteQuickAction(quickAction: QuickActionEntity)

    @Query("DELETE FROM ${INFO.TABLE_NAME_QUICK_ACTION}")
    suspend fun deleteAllQuickActions()

    @Query("UPDATE ${INFO.TABLE_NAME_QUICK_ACTION} SET itemName = :itemName WHERE position = :position")
    suspend fun updateQuickActionItemByPosition(position: Int, itemName: String)

    @androidx.room.Transaction
    suspend fun replaceAllQuickActions(quickActions: List<QuickActionEntity>) {
        deleteAllQuickActions()
        insertAllQuickActions(quickActions)
    }
}
