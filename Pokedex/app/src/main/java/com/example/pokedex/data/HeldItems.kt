package com.example.pokedex.data

data class HeldItems (
    val item: Item,
    val version_details: List<VersionDetails>
)

data class Item (
    val name: String,
    val url: String
)

data class VersionDetails (
    val rarity: Int,
    val version: Version
)

data class Version (
    val name: String,
    val url: String
)
