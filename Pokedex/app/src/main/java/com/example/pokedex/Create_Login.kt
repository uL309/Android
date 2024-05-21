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
import com.example.pokedex.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
            val user = User(0,login.text.toString(), password.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                db.userDao().insert(user)
            }
            val returnIntent= Intent()
            setResult(RESULT_OK,returnIntent)
            finish()

        }

    }
}