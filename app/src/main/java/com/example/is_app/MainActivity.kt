package com.example.is_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.*
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MehanikiAdapter
    private val mehanikiList = mutableListOf<Mehanik>()

    // API konfiguracija
    private val BASE_URL = "https://autoservis-is-hhexe2azgca8ecen.italynorth-01.azurewebsites.net"
    private val API_KEY = "SecretKey"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializacija Volley request queue
        requestQueue = Volley.newRequestQueue(this)

        // Inicializacija UI elementov
        recyclerView = findViewById(R.id.recyclerView)
        val btnNalozi = findViewById<Button>(R.id.btnNalozi)
        val btnDodaj = findViewById<Button>(R.id.btnDodaj)
        val etIme = findViewById<EditText>(R.id.etIme)
        val etPriimek = findViewById<EditText>(R.id.etPriimek)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTelefon = findViewById<EditText>(R.id.etTelefon)

        // Nastavitev RecyclerView
        adapter = MehanikiAdapter(mehanikiList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Gumb za nalaganje mehanikov (READ operacija)
        btnNalozi.setOnClickListener {
            loadMehaniki()
        }

        // Gumb za dodajanje novega mehanika (CREATE operacija)
        btnDodaj.setOnClickListener {
            val ime = etIme.text.toString().trim()
            val priimek = etPriimek.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefon = etTelefon.text.toString().trim()

            if (ime.isEmpty() || priimek.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Izpolnite vsaj ime, priimek in email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            createMehanik(ime, priimek, email, telefon)
        }
        loadMehaniki()
    }

    // READ operacija - Pridobi vse mehanike
    private fun loadMehaniki() {
        val url = "$BASE_URL/api/v1/mehaniki"

        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                mehanikiList.clear()
                for (i in 0 until response.length()) {
                    val mehanikJson = response.getJSONObject(i)
                    val mehanik = Mehanik(
                        mehanikID = mehanikJson.getInt("mehanikID"),
                        ime = mehanikJson.getString("ime"),
                        priimek = mehanikJson.getString("priimek"),
                        email = mehanikJson.getString("email"),
                        telefon = mehanikJson.optString("telefon", ""),
                        specializacija = mehanikJson.optString("specializacija", "")
                    )
                    mehanikiList.add(mehanik)
                }
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Naloženih ${mehanikiList.size} mehanikov", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(this, "Napaka pri nalaganju: ${error.message}", Toast.LENGTH_SHORT).show()
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

    // CREATE operacija - Ustvari novega mehanika
    private fun createMehanik(ime: String, priimek: String, email: String, telefon: String) {
        val url = "$BASE_URL/api/v1/mehaniki"

        val jsonBody = JSONObject().apply {
            put("ime", ime)
            put("priimek", priimek)
            put("email", email)
            put("telefon", telefon)
            // Dodaj ostale polja, če jih potrebuješ
        }

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Mehanik uspešno dodan!", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.etIme).text.clear()
                findViewById<EditText>(R.id.etPriimek).text.clear()
                findViewById<EditText>(R.id.etEmail).text.clear()
                findViewById<EditText>(R.id.etTelefon).text.clear()
                loadMehaniki()
            },
            { error ->
                Toast.makeText(this, "Napaka pri dodajanju: ${error.message}", Toast.LENGTH_SHORT).show()
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