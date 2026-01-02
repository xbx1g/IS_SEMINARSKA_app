package com.example.is_app

data class Vozilo(
    val voziloID: Int,
    val znamka: String,
    val model: String,
    val registracija: String,
    val letnik: Int,
    val vin: String,
    val lastnikID: String?
)