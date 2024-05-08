package com.example.pokedex.data

data class PokeList(val abilities: List<Abilities>,
                    val base_experience: Int,
                    val height: Int,
                    val held_items: List<HeldItems>,
                    val id: Int,
                    val moves: List<Moves>,
                    val name: String,
                    val stats: List<Stats>,
                    val types: List<Types>,
                    val weight: Int)
