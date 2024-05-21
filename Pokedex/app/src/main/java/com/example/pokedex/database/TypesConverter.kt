package com.example.pokedex.database

import androidx.room.TypeConverter
import com.example.pokedex.data.Types
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypesConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromTypesList(types: List<Types>): String {
        return gson.toJson(types)
    }

    @TypeConverter
    fun toTypesList(typesString: String): List<Types> {
        val type = object : TypeToken<List<Types>>() {}.type
        return gson.fromJson(typesString, type)
    }
}
