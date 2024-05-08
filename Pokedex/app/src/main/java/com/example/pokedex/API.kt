package com.example.pokedex

import com.example.pokedex.data.PokeList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface API {
    @GET("pokemon/{id}")
    fun getPokemon(@Path("id") id: Int): Call<PokeList>

}