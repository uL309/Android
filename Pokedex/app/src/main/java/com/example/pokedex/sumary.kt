package com.example.pokedex

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class sumary : AppCompatActivity() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sumary)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewModel = ViewModelProvider(this)[SumaryViewModel::class.java]
        val number = intent.getIntExtra("number", 0)
        Thread {
            val Api=retrofit.create(API::class.java).getPokemon(number).execute()
            if (Api.isSuccessful){
                val pokemon=Api.body()
                if (pokemon!=null){
                    runOnUiThread {
                        viewModel.pokemon.value = pokemon
                        val fragment = SumaryFragment()
                        supportFragmentManager.beginTransaction().replace(R.id.main, fragment).commit()
                    }
                }
            }
        }.start()
    }
}