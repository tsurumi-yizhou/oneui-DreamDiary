package com.snow.diary.io.data.entity

import com.snow.diary.model.data.Person
import kotlinx.serialization.Serializable

@Serializable
data class PersonEntity(

    val id: Long? = null,

    val name: String,

    val isFavourite: Boolean,

    val relationId: Long,

    val notes: String?

){
    fun toModel() = Person(id, name, isFavourite, relationId, notes)

    constructor(person: Person) : this(
        person.id, person.name, person.isFavourite, person.relationId, person.notes
    )

}