package com.example.catalogoproductos.models 

data class Producto(
    val nombre: String,
    val marca: String,
    val precio: Double,
    val imagen: Int,
    val descripcion: String,
    val notas: List<String>,
    var esFavorito: Boolean = false
)
