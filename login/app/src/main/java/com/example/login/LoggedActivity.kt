package com.example.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.login.databinding.ActivityLoggedBinding


class LoggedActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoggedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email= binding.EmailAddress.text.toString()
        val senha= binding.Senha.text.toString()
        val result= "$email $senha"
        binding.button.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("resultKey", result)
            setResult(RESULT_OK, resultIntent)
            finish()
            setContentView(R.layout.activity_main)
        }



    }

}