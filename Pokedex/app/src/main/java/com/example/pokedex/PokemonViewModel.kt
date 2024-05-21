package com.example.pokedex

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokedex.database.DatabaseProvider

import com.example.pokedex.database.PokeData

class PokemonViewModel(private val context: Context) : ViewModel() {
    var Pokemon = MutableLiveData<List<PokeData?>>()

    init {
        carregarPokemon()
    }

    private fun carregarPokemon() {
        Thread {
            val pokeDataDao = DatabaseProvider.getDatabase(context).pokeDataDao()
            val pokemonsFromDb = pokeDataDao.allPokemon()
            Pokemon.postValue(pokemonsFromDb)
        }.start()
    }
}

