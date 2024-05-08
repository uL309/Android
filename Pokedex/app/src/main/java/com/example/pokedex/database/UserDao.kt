package com.example.pokedex.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM user ")
    fun allNames(name: List<User>): User

    @Insert
    fun insert(vararg users: User)

    @Delete
    fun delete(user: User)

}