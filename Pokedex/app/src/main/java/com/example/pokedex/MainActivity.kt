package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.example.pokedex.database.AppDatabase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private val button1:Button = findViewById(R.id.button)
    private val button2:Button = findViewById(R.id.button2)
    private var login:TextView = findViewById(R.id.login)
    private var password:TextView = findViewById(R.id.Password)
    private var logged:Boolean = false
    private val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "pokedex-database"
    ).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Firstload(db,retrofit).firstload()
        button1.setOnClickListener {
            logged=login(login.text.toString(),password.text.toString())
            if (logged){
                // Go to next activity
            } else {
                // Show error message
            }
        }
        button2.setOnClickListener {
            val intent = Intent(this, Create_Login::class.java)
        }


    }

    fun login(name: String, password: String): Boolean{
        // Login logic
        return true
    }
}