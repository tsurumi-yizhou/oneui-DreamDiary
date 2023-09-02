package com.snow.diary.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.snow.diary.database.model.RelationEntity
import com.snow.diary.database.model.RelationWithPersons
import kotlinx.coroutines.flow.Flow

@Dao
interface RelationDao {

    @Transaction
    @Insert
    fun insert(vararg entity: RelationEntity): List<Long>

    @Transaction
    @Update
    fun update(vararg entity: RelationEntity)

    @Transaction
    @Delete
    fun delete(vararg entity: RelationEntity)

    @Transaction
    @Query("SELECT * FROM person_relation")
    fun getAll(): Flow<List<RelationEntity>>

    @Transaction
    @Query("SELECT * FROM person_relation WHERE id = :id")
    fun getById(id: Long): Flow<RelationEntity?>

    @Transaction
    @Query("SELECT * FROM person_relation")
    fun getAllRelationsWithPersons(): Flow<List<RelationWithPersons>>

    @Transaction
    @Query("SELECT * FROM person_relation WHERE id = :id")
    fun getRelationWithPersonsById(id: Long): Flow<RelationWithPersons>
}