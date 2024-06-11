package com.example.quiz.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(
    tableName = "leaderboard",
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["id"],
        childColumns = ["id_quiz"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Leaderboard(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "id_quiz") val id_quiz : Int,
    @ColumnInfo(name = "nome") val nome : String,
    @ColumnInfo(name = "pontuacao") val pontuacao : Int
)
