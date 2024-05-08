package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorite")
    fun allFavorite(name: List<Favorite>): Favorite
    @Insert
    fun insert(vararg favorites: Favorite)
    @Delete
    fun delete(favorite: Favorite)
}