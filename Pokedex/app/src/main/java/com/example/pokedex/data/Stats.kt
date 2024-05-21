package com.example.pokedex.data

data class Stats(
    val base_stat: Int,
    val effort: Int,
    val stat: Stat
){
    fun getStatName() = stat.name
}

data class Stat(
    val name: String,
    val url: String
)
