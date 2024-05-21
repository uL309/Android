package com.example.pokedex.data

data class Types (
    val slot: Int,
    val type: Type
) {
    val name: String
        get() = type.name
}

data class Type (
    val name: String,
    val url: String
)
