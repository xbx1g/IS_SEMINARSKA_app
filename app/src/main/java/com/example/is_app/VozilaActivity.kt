package com.example.is_app

import android.os.Bundle
import android.util.Log
import android.widget.*
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

class VozilaActivity : AppCompatActivity() {

    private lateinit var requestQueue: RequestQueue
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: VozilaAdapter
    private val vozilaList = mutableListOf<Vozilo>()

    private val BASE_URL = "https://autoservis-is-hhexe2azgca8ecen.italynorth-01.azurewebsites.net"
    private val API_KEY = "SecretKey" // TODO: Spremeni na pravi ključ!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vozila)

        requestQueue = Volley.newRequestQueue(this)

        recyclerView = findViewById(R.id.recyclerView)
        val btnNalozi = findViewById<Button>(R.id.btnNalozi)
        val btnDodaj = findViewById<Button>(R.id.btnDodaj)
        val etZnamka = findViewById<EditText>(R.id.etZnamka)
        val etModel = findViewById<EditText>(R.id.etModel)
        val etRegistracija = findViewById<EditText>(R.id.etRegistracija)
        val etLetnik = findViewById<EditText>(R.id.etLetnik)
        val btnNazaj = findViewById<Button>(R.id.btnNazaj)

        adapter = VozilaAdapter(vozilaList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnNalozi.setOnClickListener {
            loadVozila()
        }

        btnDodaj.setOnClickListener {
            val znamka = etZnamka.text.toString().trim()
            val model = etModel.text.toString().trim()
            val registracija = etRegistracija.text.toString().trim()
            val letnik = etLetnik.text.toString().trim()

            if (znamka.isEmpty() || model.isEmpty() || registracija.isEmpty()) {
                Toast.makeText(this, "Izpolnite vsaj znamko, model in registracijo", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

        }

        btnNazaj.setOnClickListener {
            finish()
        }

        loadVozila()
    }

    private fun loadVozila() {
        val url = "$BASE_URL/api/v1/vozila"

        val jsonArrayRequest = object : JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                vozilaList.clear()
                for (i in 0 until response.length()) {
                    val voziloJson = response.getJSONObject(i)
                    val vozilo = Vozilo(
                        voziloID = voziloJson.getInt("voziloID"),
                        znamka = voziloJson.getString("znamka"),
                        model = voziloJson.getString("model"),
                        registracija = voziloJson.getString("registracija"),
                        letnik = voziloJson.optInt("letnik", 0),
                        lastnikID = voziloJson.optString("lastnikId", null)
                    )
                    vozilaList.add(vozilo)
                }
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Naloženih ${vozilaList.size} vozil", Toast.LENGTH_SHORT).show()
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

    private fun createVozilo(znamka: String, model: String, registracija: String, letnik: Int, vin: String) {
        val url = "$BASE_URL/api/v1/vozila"

        val jsonBody = JSONObject().apply {
            put("znamka", znamka)
            put("model", model)
            put("registracija", registracija)
            put("letnik", letnik)
            put("vin", vin)
            put("datumUstvarjen", LocalDate.now().toString())
        }

        val stringRequest = object : StringRequest(
            Request.Method.POST, url,
            { response ->
                Toast.makeText(this, "Vozilo dodano!", Toast.LENGTH_SHORT).show()
                findViewById<EditText>(R.id.etZnamka).text.clear()
                findViewById<EditText>(R.id.etModel).text.clear()
                findViewById<EditText>(R.id.etRegistracija).text.clear()
                findViewById<EditText>(R.id.etLetnik).text.clear()
                loadVozila()
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