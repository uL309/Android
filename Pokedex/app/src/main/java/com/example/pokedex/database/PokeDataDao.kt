package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PokeDataDao {
    @Query("SELECT * FROM pokedata")
    fun allPokemon(): List<PokeData>

    @Insert
    fun insert(vararg pokedatas: PokeData)

    @Delete
    fun delete(pokeData: PokeData)
}