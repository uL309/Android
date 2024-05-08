package com.example.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.pokedex.data.Types

@Entity
data class PokeData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "type") val type: List<Types>,
    @ColumnInfo(name = "path") val photo: String? = null
)

