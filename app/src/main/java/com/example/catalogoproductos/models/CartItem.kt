package com.example.catalogoproductos.models

data class CartItem(
    val id: Long,
    val nombre: String,
    val marca: String,
    val precio: Double,
    val imagen: Int,
    var cantidad: Int
) {
    fun total(): Double = precio * cantidad
}
