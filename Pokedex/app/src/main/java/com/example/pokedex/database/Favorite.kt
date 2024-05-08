package com.example.pokedex.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(foreignKeys = [ForeignKey(
    entity = User::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("idUser"),
    onDelete = ForeignKey.CASCADE
),ForeignKey(
    entity = PokeData::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("idPoke"),
    onDelete = ForeignKey.CASCADE
)],
primaryKeys = ["idUser", "idPoke"]
)


data class Favorite(

    val idUser: Int,
    val idPoke: Int
)