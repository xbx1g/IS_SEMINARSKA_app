package com.example.is_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.time.LocalDate

class StrankeActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StrankeAdapter
    private val strankeList = mutableListOf<Stranka>()

    private val BASE_URL = "https://autoservis-is-hhexe2azgca8ecen.italynorth-01.azurewebsites.net"
    private val API_KEY = "SecretKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stranke)

        requestQueue = Volley.newRequestQueue(this)

        recyclerView = findViewById(R.id.recyclerView)
        val btnNalozi = findViewById<Button>(R.id.btnNalozi)
        val btnDodaj = findViewById<Button>(R.id.btnDodaj)
        val etIme = findViewById<EditText>(R.id.etIme)
        val etPriimek = findViewById<EditText>(R.id.etPriimek)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTelefon = findViewById<EditText>(R.id.etTelefon)
        val btnNazaj = findViewById<Button>(R.id.btnNazaj)

        adapter = StrankeAdapter(strankeList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnNalozi.setOnClickListener {
            loadStranke()
        }

        btnDodaj.setOnClickListener {
            val ime = etIme.text.toString().trim()
            val priimek = etPriimek.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefon = etTelefon.text.toString().trim()

            if (ime.isEmpty() || priimek.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Izpolnite vsaj ime, priimek in email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createStranka(ime, priimek, email, telefon)
        }

        btnNazaj.setOnClickListener {
            finish()
        }

        loadStranke()
    }

    private fun loadStranke() {
        val url = "$BASE_URL/api/v1/stranke"

        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                strankeList.clear()
                for (i in 0 until response.length()) {
                    val strankaJson = response.getJSONObject(i)
                    val stranka = Stranka(
                        id = strankaJson.getInt("id"),
                        ime = strankaJson.getString("ime"),
                        priimek = strankaJson.getString("priimek"),
                        email = strankaJson.getString("email"),
                        telefon = strankaJson.optString("telefon", ""),
                        datumRegistracije = strankaJson.optString("datumRegistracije", "")
                    )
                    strankeList.add(stranka)
                }
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Naloženih ${strankeList.size} strank", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Napaka: ${error.message}", Toast.LENGTH_SHORT).show()
                Log.e("API", "Error: ${error.message}")
            }
        ) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["ApiKey"] = API_KEY
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(jsonArrayRequest)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createStranka(ime: String, priimek: String, email: String, telefon: String) {
        val url = "$BASE_URL/api/v1/stranke"

        val jsonBody = JSONObject().apply {
            put("ime", ime)
            put("priimek", priimek)
            put("email", email)
            put("telefon", telefon)
            put("datumRegistracije", LocalDate.now().toString())
        }

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Stranka dodana!", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.etIme).text.clear()
                findViewById<EditText>(R.id.etPriimek).text.clear()
                findViewById<EditText>(R.id.etEmail).text.clear()
                findViewById<EditText>(R.id.etTelefon).text.clear()
                loadStranke()
            },
            { error ->
                Toast.makeText(this, "Napaka: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun getBody(): ByteArray {
                return jsonBody.toString().toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["ApiKey"] = API_KEY
                headers["Content-Type"] = "application/json"
                return headers
            }
        }

        requestQueue.add(stringRequest)
    }
}