package com.example.pokedex.data

data class HeldItems (
    val item: Item,
    val version_details: List<VersionDetails>
){
    fun getItemName(): String {
        return item.name
    }

    fun getrarity(): List<Int> {
        val rarity = mutableListOf<Int>()
        version_details.forEach(){
            rarity.add(it.rarity)
        }
        return rarity
    }

    fun getVersion(): List<String> {
        val version = mutableListOf<String>()
        version_details.forEach(){
            version.add(it.version.name)
        }
        return version
    }
}

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
