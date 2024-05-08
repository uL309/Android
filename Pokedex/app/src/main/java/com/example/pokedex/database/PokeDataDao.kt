package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.pokedex.data.PokeList

@Dao
interface PokeDataDao {
    @Query("SELECT * FROM pokedata")
    fun allPokemon(name: List<PokeData>): PokeData

    @Insert
    fun insert(vararg pokedatas: PokeList)

    @Delete
    fun delete(pokeData: PokeData)
}