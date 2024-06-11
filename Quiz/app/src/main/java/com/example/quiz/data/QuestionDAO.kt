package com.example.quiz.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface QuestionDAO {
    @Query("SELECT * FROM Question")
    fun allNames(): List<Question>

    @Query("SELECT * FROM Question WHERE id_quiz = :id")
    fun findById(id: Long): List<Question>

    @Update
    fun update(question: Question)

    @Insert
    fun inserirRegistro(question: Question)
}