package com.example.catalogoproductos.models

data class Order(
    val id: Long,
    val fecha: Long,
    val total: Double,
    val itemsCount: Int
)
