package com.example.quiz.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase.deleteDatabase
import androidx.room.Room
import com.example.quiz.R

object DatabaseProvider {
    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        context.deleteDatabase("quiz-database")
        if (db == null) {
            db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "quiz-database"
            ).build()
            db!!.quizDAO().inserirRegistro(Quiz(0, "Pokemon Quiz"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz0, "Qual é o Pokémon inicial da região de Kanto na primeira geração?", "Bulbasaur", "Charmander"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz01, "Qual é o tipo primário do Pikachu?", "Elétrico", "Fogo"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz02, "Qual destes Pokémon evolui para Charizard?", "Charmeleon", "Charmander"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz03, "Qual é o tipo do Pokémon Mewtwo?", "Psíquico", "Sombrio"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz04, "Qual destes Pokémon é conhecido como 'o guardião do mar'?", "Lugia", "Kyogre"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz05, "Qual é o tipo primário do Pokémon Bulbasaur?", "Planta", "Venenoso"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz06, "Qual destes Pokémon evolui para Blastoise?", "Warturtle", "Squirtle"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz07, "Qual é o tipo primário do Pokémon Gengar?", "Fantasma", "Venenoso"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz08, "Qual destes Pokémon é conhecido como 'o Pokémon original'?", "Pikachu", "Eevee"))
            db!!.questionDAO().inserirRegistro(Question(0, 1, R.drawable.id_quiz09, "Qual o tipo primário do Pokémon Lucario?", "Lutador", "Aço"))
        }
        return db!!
    }
}