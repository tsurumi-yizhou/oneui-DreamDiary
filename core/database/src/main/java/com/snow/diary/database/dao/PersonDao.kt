package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.snow.diary.database.model.PersonEntity
import com.snow.diary.database.model.cross.PersonWithDreams
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {
    @Transaction
    @Upsert
    fun upsert(vararg person: PersonEntity)

    @Transaction
    @Delete
    fun delete(vararg person: PersonEntity)

    @Transaction
    @Query("SELECT * FROM person")
    fun getAllPersons(): Flow<List<PersonEntity>>

    @Transaction
    @Query("SELECT * FROM person WHERE personId = :id")
    fun getPersonById(id: Long): Flow<PersonEntity?>

    @Transaction
    @Query("SELECT * FROM person WHERE personId = :id")
    fun getPersonWithDreamsById(id: Long): Flow<PersonWithDreams?>

    @Transaction
    @Query("SELECT * FROM person")
    fun getAllPersonsWithDreams(): Flow<List<PersonWithDreams>>
}