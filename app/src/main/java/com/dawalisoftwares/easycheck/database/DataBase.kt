package com.dawalisoftwares.easycheck.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dawalisoftwares.easycheck.adapters.BeePerson

@Database(entities = [BeePerson::class], version = 1)
@TypeConverters(phoneNumberListConverter::class)
abstract class DataBase : RoomDatabase() {
    abstract fun beePersonDao(): BeePersonDao
}