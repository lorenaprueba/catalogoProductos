package com.example.catalogoproductos.models

data class OrderItem(
    val orderId: Long,
    val nombre: String,
    val marca: String,
    val precio: Double,
    val imagen: Int,
    val cantidad: Int
) {
    fun total(): Double = precio * cantidad
}
