package com.example.pokedex

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pokedex.database.AppDatabase
import com.example.pokedex.database.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {
    private lateinit var button1: Button
    private lateinit var button2: Button
    private lateinit var login: TextView
    private lateinit var password: TextView
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var logged:Boolean = false
    private lateinit var intent: Intent
    private lateinit var db: AppDatabase
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
        button1 = findViewById(R.id.button)
        button2 = findViewById(R.id.button2)
        login = findViewById(R.id.login)
        password = findViewById(R.id.Password)
        db = DatabaseProvider.getDatabase(this)

        Firstload(db,retrofit).firstload()

        button1.setOnClickListener {
            logged=login(login.text.toString(),password.text.toString())
            if (logged){
                intent = Intent(this, list::class.java)
                startActivity(intent)
            } else {
                showPopupMessage("Login ou senha incorretos")
            }
        }
        button2.setOnClickListener {
            intent = Intent(this, Create_Login::class.java)
            startActivity(intent)
        }


    }

    private fun login(name: String, password: String): Boolean {
        return runBlocking {
            val user = coroutineScope.async(Dispatchers.IO) {
                db.userDao().findByName(name)
            }.await()

            user?.password == password
        }
    }

    fun showPopupMessage(message: String) {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.popup_layout, null)

        val textView = view.findViewById<TextView>(R.id.popupText)
        textView.text = message

        val popupWindow = PopupWindow(
            view,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        popupWindow.isFocusable = true

        val location = IntArray(2)
        // Coloque a posição do popup na tela no centro
        val parent = findViewById<View>(R.id.main)
        parent.getLocationOnScreen(location)
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0)

        // Defina um tempo para o popup ser fechado após algum tempo, por exemplo, 2 segundos
        Handler().postDelayed({ popupWindow.dismiss() }, 2000)
    }

}