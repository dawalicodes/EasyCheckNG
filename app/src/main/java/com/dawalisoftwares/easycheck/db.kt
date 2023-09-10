package com.dawalisoftwares.easycheck

import android.content.Context
import androidx.room.Room
import com.dawalisoftwares.easycheck.database.DataBase
import com.dawalisoftwares.easycheck.database.DatabaseInfo

object db {
    private var database: DataBase? = null
    fun getDataBase(context: Context): DataBase? {
        if (database != null) {
            return database
        }
        database =
            Room.databaseBuilder(context, DataBase::class.java, DatabaseInfo.DATABASE_NAME).build()
        return database
    }
}