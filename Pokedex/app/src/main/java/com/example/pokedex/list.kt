package com.example.pokedex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pokedex.database.PokeData

class list : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    private val viewModel by lazy{
        ViewModelProvider(this, PokemonViewModelFactory(this))
            .get(PokemonViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        recyclerView = findViewById(R.id.rvPokemon)
        viewModel.Pokemon.observe(this, Observer {
            loadRecyclerView(it)
        })
    }

    private fun loadRecyclerView(pokemons: List<PokeData?>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = PokemonAdapter(pokemons)
    }
}