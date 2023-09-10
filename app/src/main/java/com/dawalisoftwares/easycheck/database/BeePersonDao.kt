package com.dawalisoftwares.easycheck.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.dawalisoftwares.easycheck.adapters.BeePerson

@Dao
interface BeePersonDao {
    @Query(value = "SELECT * FROM ${DatabaseInfo.TABLE_NAME_BEE_PERSON}")
    fun getAllPersons(): LiveData<List<BeePerson>>

    @Insert
    fun insertBeePersons(vararg persons: BeePerson)

    @Delete
    fun deleteBeePerson(person: BeePerson)
}