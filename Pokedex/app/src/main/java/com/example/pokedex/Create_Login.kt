package com.example.pokedex

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokedex.database.DatabaseProvider

class Create_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button=findViewById<Button>(R.id.button)
        val login=findViewById<EditText>(R.id.login)
        val password=findViewById<EditText>(R.id.Password)
        val db = DatabaseProvider.getDatabase(this)

        button.setOnClickListener {
            db.userDao().insert(com.example.pokedex.database.User(0,login.text.toString(),password.text.toString()))
            val returnIntent= Intent()
            setResult(RESULT_OK,returnIntent)
            finish()

        }

    }
}