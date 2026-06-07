package com.example.panini.wc2026.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "laminas")
data class Lamina(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val numero: String,       // Número visible en la lámina (ej: "COL-1")
    val nombre: String,       // Nombre del jugador
    val pais: String,         // Selección
    val grupo: String,        // Grupo del álbum (ej: "COLOMBIA", "BRASIL", "ICONOS")
    var obtenida: Boolean = false,
    var cantidadRepetidas: Int = 0
)
