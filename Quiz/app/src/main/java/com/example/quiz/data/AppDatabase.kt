package com.example.quiz.data

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Quiz::class, Question::class, Leaderboard::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun quizDAO(): QuizDAO
    abstract fun questionDAO(): QuestionDAO
    abstract fun leaderboardDAO(): LeaderboardDao

}
