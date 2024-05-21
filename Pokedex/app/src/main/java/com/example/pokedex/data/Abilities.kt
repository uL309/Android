package com.example.pokedex.data

data class Abilities (
    val ability: Ability,
    val is_hidden: Boolean,
    val slot: Int
){
    fun getAbilityName(): String {
        return ability.name
    }

}

data class Ability (
    val name: String,
    val url: String
)


