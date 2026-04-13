package com.example.catalogoproductos.models 

object Carrito {

    private val listaProductos = mutableListOf<Producto>()

    fun agregar(producto: Producto) {
        listaProductos.add(producto)
    }

    fun eliminar(producto: Producto) {
        listaProductos.remove(producto)
    }

    fun obtenerProductos(): List<Producto> {
        return listaProductos
    }

    fun obtenerTotal(): Double {
        return listaProductos.sumOf { it.precio }
    }
}