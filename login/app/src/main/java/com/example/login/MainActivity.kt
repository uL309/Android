package com.example.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.login.R.*


class MainActivity : AppCompatActivity() {
    private lateinit var email: String
    private lateinit var senha: String
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val button = findViewById<TextView>(id.button)
        email= "surdomudo@cegomail.com"
        senha= "123456"
        while (true) {
            button.setOnClickListener {
                val emailTela= findViewById<EditText>(id.EmailAddress)
                val senhaTela = findViewById<EditText>(id.Senha)
                if (email == emailTela.text.toString() && senha == senhaTela.text.toString()) {
                    setContentView(layout.activity_logged)
                } else {
                    findViewById<TextView>(id.textView).text = "Email ou senha incorretos"
                }

            }
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("resultKey")
            val listResult: List<String> = result!!.split(" ")
            email=listResult[0]
            senha=listResult[1]
        }
    }

}