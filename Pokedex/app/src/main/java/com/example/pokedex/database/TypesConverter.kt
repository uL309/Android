package com.example.pokedex.database

import androidx.room.TypeConverter
import com.example.pokedex.data.Type
import com.example.pokedex.data.Types
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TypesConverter {
    @TypeConverter
    fun fromTypes(types: Types): String {
        return Gson().toJson(types)
    }

    @TypeConverter
    fun toTypes(typesString: String): Types {
        val type = object : TypeToken<Types>() {}.type
        return Gson().fromJson(typesString, type)
    }

    @TypeConverter
    fun fromType(type: Type): String {
        return Gson().toJson(type)
    }

    @TypeConverter
    fun toType(typeString: String): Type {
        return Gson().fromJson(typeString, Type::class.java)
    }
}