package com.example.quiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LeaderboardDao {
    @Insert
    fun inserirRegistro(leaderboard: Leaderboard)
    @Query("SELECT * FROM Leaderboard")
    fun allNames(): List<Leaderboard>

}