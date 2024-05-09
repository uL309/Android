package com.example.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.pokedex.data.Types

@Entity
data class PokeData(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String,
    @TypeConverters(TypesConverter::class) @ColumnInfo(name = "type") val type: Types,
    @ColumnInfo(name = "path") val photo: String? = null
)

