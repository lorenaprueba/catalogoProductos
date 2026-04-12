package com.example.catalogoproductos.models

// Clase de datos que representa un producto en el carrito
// Contiene la información necesaria para mostrar y gestionar un producto
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    var quantity: Int = 1
) {
    // Calcula el precio total del producto (precio unitario * cantidad)
    fun getTotalPrice(): Double = price * quantity
}
