package com.example.calculadora


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.calculadora.R.*
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(id.teste)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        /* val entrada = findViewById<TextInputLayout>(R.id.input).text.toString()
        val operacao = mutableListOf<String>()
        var resultadoconta = ""
        val button = findViewById<Button>(R.id.button)
        button.text = "Calcular"
        button.setOnClickListener {


                entrada.split(" ").forEach {
                    operacao.add(it)
                }
                if (operacao[1] == "+") {
                    resultadoconta = (operacao[0].toInt() + operacao[1].toInt()).toString()
                } else if (operacao[1] == "-") {
                    resultadoconta = (operacao[0].toInt() - operacao[1].toInt()).toString()
                } else if (operacao[1] == "x" && operacao[1] == "*" && operacao[1] == "X") {
                    resultadoconta = (operacao[0].toInt() * operacao[1].toInt()).toString()
                } else if (operacao[1] == "/" && operacao[1] == "÷") {
                    resultadoconta = (operacao[0].toInt() / operacao[1].toInt()).toString()
                } else {
                    resultadoconta = "Operação inválida"
                }

            findViewById<TextView>(R.id.resultado).text=resultadoconta
        }*/
        val button = findViewById<Button>(R.id.button)
        button.text = "Calcular"
        button.setOnClickListener {
            val entrada = findViewById<TextInputLayout>(R.id.input).editText?.text.toString()

            val operacao: MutableList<String> = entrada.split(" ") as MutableList<String>
            val resultadoconta: CharSequence

            if (operacao.size == 3) {
                val num1 = operacao[0].toIntOrNull()
                val operador = operacao[1]
                val num2 = operacao[2].toIntOrNull()

                resultadoconta = if (num1 != null && num2 != null) {
                    when (operador) {
                        "+" -> (num1 + num2).toString()
                        "-" -> (num1 - num2).toString()
                        "*", "x", "X" -> (num1 * num2).toString()
                        "/", "÷" -> if (num2 != 0) (num1 / num2).toString() else "Divisão por zero"
                        else -> "Operador inválido"
                    }
                } else {
                    "Operando inválido"
                }
            } else {
                resultadoconta = "Formato inválido"
            }

            findViewById<TextView>(R.id.resultado).text = resultadoconta
        }
    }
}