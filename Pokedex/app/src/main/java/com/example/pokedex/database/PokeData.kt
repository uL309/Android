package com.example.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PokeData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "type")val type: String,
    @ColumnInfo(name = "path")val photo: String
)

