package com.dawalisoftwares.easycheck.database

import androidx.room.TypeConverter

class phoneNumberListConverter {

    @TypeConverter
    fun fromArrayToString(value: Array<String>): String {
        return value.joinToString(separator = ",")
    }

    @TypeConverter
    fun fromStringToArray(value: String): Array<String> {
        return value.split(",").toTypedArray()
    }
}
