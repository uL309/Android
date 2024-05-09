package com.example.pokedex.database

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index("idUser"), Index("idPoke")],
    foreignKeys = [
        ForeignKey(
            entity = PokeData::class,
            parentColumns = ["id"],
            childColumns = ["idPoke"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["idUser"],
            onDelete = ForeignKey.CASCADE
        )
    ])


data class Favorite(

    @PrimaryKey val id: Int,
    val idUser: Int,
    val idPoke: Int
)