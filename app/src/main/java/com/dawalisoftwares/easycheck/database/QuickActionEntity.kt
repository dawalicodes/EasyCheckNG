package com.dawalisoftwares.easycheck.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dawalisoftwares.easycheck.INFO

@Entity(
    tableName = INFO.TABLE_NAME_QUICK_ACTION,
    foreignKeys = [
        ForeignKey(
            entity = ItemEntity::class,
            parentColumns = ["name"],
            childColumns = ["itemName"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["itemName"], unique = true),
        Index(value = ["position"], unique = true)
    ]
)
data class QuickActionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "itemName")
    val itemName: String,
    @ColumnInfo(name = "position")
    val position: Int
)
