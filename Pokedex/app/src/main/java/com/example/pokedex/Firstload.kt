package com.example.pokedex

import com.example.pokedex.database.AppDatabase
import retrofit2.Retrofit

class Firstload(Database: AppDatabase, val retrofit: Retrofit) {
    private val db = Database
    fun firstload(){
        for (i in 1..1302){
            db.pokeDataDao().insert(retrofit.create(API::class.java).getPokemon(i).execute().body()!!)
        }
    }
}