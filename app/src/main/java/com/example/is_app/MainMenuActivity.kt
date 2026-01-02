package com.example.is_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainMenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val btnMehaniki = findViewById<Button>(R.id.btnMehaniki)
        val btnStranke = findViewById<Button>(R.id.btnStranke)
        val btnVozila = findViewById<Button>(R.id.btnVozila)

        btnMehaniki.setOnClickListener {
            val intent = Intent(this, MehanikiActivity::class.java)
            startActivity(intent)
        }

        btnStranke.setOnClickListener {
            val intent = Intent(this, StrankeActivity::class.java)
            startActivity(intent)
        }

        btnVozila.setOnClickListener {
            val intent = Intent(this, VozilaActivity::class.java)
            startActivity(intent)
        }

    }
}