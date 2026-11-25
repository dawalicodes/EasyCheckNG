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
interface ItemDao {

    @Query("SELECT * FROM ${INFO.TABLE_NAME_EASY_CHECK_ITEM} ORDER BY priority DESC")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM ${INFO.TABLE_NAME_EASY_CHECK_ITEM} WHERE name = :name")
    suspend fun getItemByName(name: String): ItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<ItemEntity>)


    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)
}