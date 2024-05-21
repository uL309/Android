package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PokeDataDao {
    @Query("SELECT * FROM pokedata")
    fun allPokemon(): List<PokeData>

    @Query("SELECT * FROM pokedata WHERE id = :id")
    fun findPokemonById(id: Int): PokeData?

    @Insert
    fun insert(vararg pokedatas: PokeData)

    @Delete
    fun delete(pokeData: PokeData)
    @Update
    fun update(pokedata: PokeData)
}