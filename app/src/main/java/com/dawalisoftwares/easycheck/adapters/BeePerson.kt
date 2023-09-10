package com.dawalisoftwares.easycheck.adapters

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dawalisoftwares.easycheck.database.DatabaseInfo

@Entity(
    tableName = DatabaseInfo.TABLE_NAME_BEE_PERSON
)
data class BeePerson(
    @PrimaryKey(autoGenerate = true) val key: Int,
    @ColumnInfo(name = DatabaseInfo.BeePersonTableInfo.COLUMN_NAME_NAME) val name: String,
    @ColumnInfo(name = DatabaseInfo.BeePersonTableInfo.COLUMN_NAME_PHONE) val phone: Array<String>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BeePerson

        if (!phone.contentEquals(other.phone)) return false

        return true
    }

    override fun hashCode(): Int {
        return phone.contentHashCode()
    }
}