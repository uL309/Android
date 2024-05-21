package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, PokeData::class, Favorite::class], version = 1, exportSchema = false)
@TypeConverters(TypesConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun pokeDataDao(): PokeDataDao
}