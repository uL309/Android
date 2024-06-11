package com.example.quiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface QuizDAO {
    @Query("SELECT * FROM Quiz")
    fun allNames(): List<Quiz>
    @Query("SELECT * FROM Quiz WHERE id = :id")
    fun findById(id: Long): Quiz
    @Update
    fun update(quiz: Quiz)
    @Insert
    fun inserirRegistro(quiz: Quiz)
}