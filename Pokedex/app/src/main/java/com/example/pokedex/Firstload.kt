package com.example.pokedex

import com.example.pokedex.database.AppDatabase
import com.example.pokedex.database.PokeData
import retrofit2.Retrofit

class Firstload(database: AppDatabase, private val retrofit: Retrofit) {
    private val db = database
    fun firstload() {
        Thread {
            for (i in 1..1302) {
                val response = retrofit.create(API::class.java).getPokemon(i).execute()
                if (response.isSuccessful) {
                    val pokemao = response.body()
                    if (pokemao != null) {
                        val pokemon = PokeData(pokemao.id, pokemao.name, pokemao.types)
                        val existingPokemon = db.pokeDataDao().findPokemonById(pokemon.id)
                        if (existingPokemon == null) {
                            db.pokeDataDao().insert(pokemon)
                        } else {
                            db.pokeDataDao().update(pokemon)
                        }
                    }
                }
            }
        }.start()
    }

}