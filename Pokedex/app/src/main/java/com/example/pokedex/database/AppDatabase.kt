package com.example.pokedex.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class,Favorite::class,PokeData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun pokeDataDao(): PokeDataDao
}