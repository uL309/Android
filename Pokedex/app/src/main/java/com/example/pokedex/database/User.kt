package com.example.pokedex.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey (autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name")val name: String,
    @ColumnInfo(name = "password")val password: String
)
