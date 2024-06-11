package com.example.quiz.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity (tableName = "question",
    foreignKeys = [ForeignKey(
        entity = Quiz::class,
        parentColumns = ["id"],
        childColumns = ["id_quiz"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Int ,
    @ColumnInfo (name = "id_quiz")
    val id_quiz : Int,
    @ColumnInfo (name = "imagem")
    val img : Int,
    @ColumnInfo (name = "pergunta")
    val pergunta : String,
    @ColumnInfo (name = "resposta_certa")
    val resposta_certa: String,
    @ColumnInfo (name = "resposta_errada")
    val resposta_errada : String?
)
