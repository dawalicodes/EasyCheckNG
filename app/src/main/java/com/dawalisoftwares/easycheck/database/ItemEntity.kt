package com.dawalisoftwares.easycheck.database

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dawalisoftwares.easycheck.INFO

@Entity(tableName = INFO.TABLE_NAME_EASY_CHECK_ITEM)
data class ItemEntity(
    @PrimaryKey
    val name: String,
    val description: String,
    @field:DrawableRes
    val icon: Int,
    val iconBackgroundColor: String,
    val iconColor: String,
    val priority: Int,
    @ColumnInfo(defaultValue = "false")
    val custom: Boolean = false,
)
