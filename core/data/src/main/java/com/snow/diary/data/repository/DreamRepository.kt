package com.snow.diary.data.repository

import com.snow.diary.model.combine.DreamAggregate
import com.snow.diary.model.data.Dream
import com.snow.diary.model.sort.SortConfig
import kotlinx.coroutines.flow.Flow

interface DreamRepository {

    suspend fun insert(vararg dream: Dream): List<Long>

    suspend fun upsertDreamPersonCrossref(dreamId: Long, personId: Long)

    suspend fun upsertDreamLocationCrossref(dreamId: Long, locationId: Long)

    suspend fun deleteDreamPersonCrossref(dreamId: Long, personId: Long)

    suspend fun deleteDreamLocationCrossref(dreamId: Long, locationId: Long)

    suspend fun update(vararg dream: Dream)

    suspend fun deleteDream(vararg dream: Dream)

    fun getAllDreams(
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<Dream>>

    fun getDreamById(id: Long): Flow<Dream?>

    fun getExtendedDreamById(id: Long): Flow<DreamAggregate?>

    fun getAllExtendedDreams(
        sortConfig: SortConfig = SortConfig()
    ): Flow<List<DreamAggregate>>

    fun getDreamsByLocation(id: Long): Flow<List<Dream>?>

    fun getDreamsByPerson(id: Long): Flow<List<Dream>?>

}