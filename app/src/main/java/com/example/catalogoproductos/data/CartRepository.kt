package com.example.catalogoproductos.data

import android.content.ContentValues
import android.content.Context
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_CANTIDAD
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_ID
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_IMAGEN
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_MARCA
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_NOMBRE
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_CANTIDAD
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_ID
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_IMAGEN
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_MARCA
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_NOMBRE
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_ORDER_ID
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_OI_PRECIO
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_ORDER_FECHA
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_ORDER_ID
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_ORDER_ITEMS_COUNT
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_ORDER_TOTAL
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_PRECIO
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.COL_EMAIL
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.TABLE_CART
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.TABLE_FAVORITES
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.TABLE_ORDERS
import com.example.catalogoproductos.data.CatalogoDbHelper.Companion.TABLE_ORDER_ITEMS
import com.example.catalogoproductos.models.CartItem
import com.example.catalogoproductos.models.Order
import com.example.catalogoproductos.models.OrderItem
import com.example.catalogoproductos.models.Producto

class CartRepository(context: Context) {

    private val dbHelper = CatalogoDbHelper(context)
    private val userEmail: String

    init {
        val sharedPref = context.getSharedPreferences("USER_PREFS", Context.MODE_PRIVATE)
        userEmail = sharedPref.getString("user_email", "default@test.com") ?: "default@test.com"
    }

    fun agregarAlCarrito(producto: Producto, cantidad: Int = 1): Long {
        return agregarAlCarritoRaw(
            nombre = producto.nombre,
            marca = producto.marca,
            precio = producto.precio,
            imagen = producto.imagen,
            cantidad = cantidad
        )
    }

    private fun agregarAlCarritoRaw(
        nombre: String,
        marca: String,
        precio: Double,
        imagen: Int,
        cantidad: Int
    ): Long {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val id = db.query(
                TABLE_CART,
                arrayOf(COL_ID, COL_CANTIDAD),
                "$COL_NOMBRE = ? AND $COL_MARCA = ? AND $COL_EMAIL = ?",
                arrayOf(nombre, marca, userEmail),
                null, null, null
            ).use { c ->
                if (c.moveToFirst()) {
                    val existingId = c.getLong(0)
                    val existingQty = c.getInt(1)
                    val values = ContentValues().apply {
                        put(COL_CANTIDAD, existingQty + cantidad)
                    }
                    db.update(TABLE_CART, values, "$COL_ID = ?", arrayOf(existingId.toString()))
                    existingId
                } else {
                    val values = ContentValues().apply {
                        put(COL_NOMBRE, nombre)
                        put(COL_MARCA, marca)
                        put(COL_PRECIO, precio)
                        put(COL_IMAGEN, imagen)
                        put(COL_CANTIDAD, cantidad)
                        put(COL_EMAIL, userEmail)
                    }
                    db.insert(TABLE_CART, null, values)
                }
            }
            db.setTransactionSuccessful()
            return id
        } finally {
            db.endTransaction()
        }
    }

    fun obtenerCarrito(): List<CartItem> {
        val db = dbHelper.readableDatabase
        val items = mutableListOf<CartItem>()
        db.query(
            TABLE_CART, null, "$COL_EMAIL = ?", arrayOf(userEmail), null, null, "$COL_ID ASC"
        ).use { c ->
            val idxId = c.getColumnIndexOrThrow(COL_ID)
            val idxNombre = c.getColumnIndexOrThrow(COL_NOMBRE)
            val idxMarca = c.getColumnIndexOrThrow(COL_MARCA)
            val idxPrecio = c.getColumnIndexOrThrow(COL_PRECIO)
            val idxImagen = c.getColumnIndexOrThrow(COL_IMAGEN)
            val idxCantidad = c.getColumnIndexOrThrow(COL_CANTIDAD)
            while (c.moveToNext()) {
                items.add(
                    CartItem(
                        id = c.getLong(idxId),
                        nombre = c.getString(idxNombre),
                        marca = c.getString(idxMarca),
                        precio = c.getDouble(idxPrecio),
                        imagen = c.getInt(idxImagen),
                        cantidad = c.getInt(idxCantidad)
                    )
                )
            }
        }
        return items
    }

    fun actualizarCantidad(id: Long, cantidad: Int) {
        if (cantidad <= 0) {
            eliminarItem(id)
            return
        }
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply { put(COL_CANTIDAD, cantidad) }
        db.update(TABLE_CART, values, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun eliminarItem(id: Long) {
        val db = dbHelper.writableDatabase
        db.delete(TABLE_CART, "$COL_ID = ?", arrayOf(id.toString()))
    }

    fun eliminarPorProducto(producto: Producto) {
        val db = dbHelper.writableDatabase
        db.delete(
            TABLE_CART,
            "$COL_NOMBRE = ? AND $COL_MARCA = ? AND $COL_EMAIL = ?",
            arrayOf(producto.nombre, producto.marca, userEmail)
        )
    }

    fun vaciarCarrito() {
        val db = dbHelper.writableDatabase
        db.delete(TABLE_CART, "$COL_EMAIL = ?", arrayOf(userEmail))
    }

    fun confirmarCompra(): Long {
        val items = obtenerCarrito()
        if (items.isEmpty()) return -1L

        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val total = items.sumOf { it.total() }
            val itemsCount = items.sumOf { it.cantidad }

            val orderValues = ContentValues().apply {
                put(COL_ORDER_FECHA, System.currentTimeMillis())
                put(COL_ORDER_TOTAL, total)
                put(COL_ORDER_ITEMS_COUNT, itemsCount)
                put(COL_EMAIL, userEmail)
            }
            val orderId = db.insert(TABLE_ORDERS, null, orderValues)

            for (item in items) {
                val v = ContentValues().apply {
                    put(COL_OI_ORDER_ID, orderId)
                    put(COL_OI_NOMBRE, item.nombre)
                    put(COL_OI_MARCA, item.marca)
                    put(COL_OI_PRECIO, item.precio)
                    put(COL_OI_IMAGEN, item.imagen)
                    put(COL_OI_CANTIDAD, item.cantidad)
                }
                db.insert(TABLE_ORDER_ITEMS, null, v)
            }

            db.delete(TABLE_CART, "$COL_EMAIL = ?", arrayOf(userEmail))
            db.setTransactionSuccessful()
            return orderId
        } finally {
            db.endTransaction()
        }
    }

    fun obtenerOrdenes(): List<Order> {
        val db = dbHelper.readableDatabase
        val orders = mutableListOf<Order>()
        db.query(
            TABLE_ORDERS, null, "$COL_EMAIL = ?", arrayOf(userEmail), null, null, "$COL_ORDER_FECHA DESC"
        ).use { c ->
            val idxId = c.getColumnIndexOrThrow(COL_ORDER_ID)
            val idxFecha = c.getColumnIndexOrThrow(COL_ORDER_FECHA)
            val idxTotal = c.getColumnIndexOrThrow(COL_ORDER_TOTAL)
            val idxItemsCount = c.getColumnIndexOrThrow(COL_ORDER_ITEMS_COUNT)
            while (c.moveToNext()) {
                orders.add(
                    Order(
                        id = c.getLong(idxId),
                        fecha = c.getLong(idxFecha),
                        total = c.getDouble(idxTotal),
                        itemsCount = c.getInt(idxItemsCount)
                    )
                )
            }
        }
        return orders
    }

    fun obtenerItemsDeOrden(orderId: Long): List<OrderItem> {
        val db = dbHelper.readableDatabase
        val items = mutableListOf<OrderItem>()
        db.query(
            TABLE_ORDER_ITEMS, null,
            "$COL_OI_ORDER_ID = ?", arrayOf(orderId.toString()),
            null, null, "$COL_OI_ID ASC"
        ).use { c ->
            val idxNombre = c.getColumnIndexOrThrow(COL_OI_NOMBRE)
            val idxMarca = c.getColumnIndexOrThrow(COL_OI_MARCA)
            val idxPrecio = c.getColumnIndexOrThrow(COL_OI_PRECIO)
            val idxImagen = c.getColumnIndexOrThrow(COL_OI_IMAGEN)
            val idxCantidad = c.getColumnIndexOrThrow(COL_OI_CANTIDAD)
            while (c.moveToNext()) {
                items.add(
                    OrderItem(
                        orderId = orderId,
                        nombre = c.getString(idxNombre),
                        marca = c.getString(idxMarca),
                        precio = c.getDouble(idxPrecio),
                        imagen = c.getInt(idxImagen),
                        cantidad = c.getInt(idxCantidad)
                    )
                )
            }
        }
        return items
    }

    fun reagregarOrdenAlCarrito(orderId: Long): Int {
        val items = obtenerItemsDeOrden(orderId)
        for (item in items) {
            agregarAlCarritoRaw(
                nombre = item.nombre,
                marca = item.marca,
                precio = item.precio,
                imagen = item.imagen,
                cantidad = item.cantidad
            )
        }
        return items.size
    }

    fun agregarFavorito(producto: Producto) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(COL_NOMBRE, producto.nombre)
            put(COL_MARCA, producto.marca)
            put(COL_PRECIO, producto.precio)
            put(COL_IMAGEN, producto.imagen)
            put(COL_EMAIL, userEmail)
        }
        db.insertWithOnConflict(TABLE_FAVORITES, null, values, android.database.sqlite.SQLiteDatabase.CONFLICT_IGNORE)
    }

    fun eliminarFavorito(producto: Producto) {
        val db = dbHelper.writableDatabase
        db.delete(TABLE_FAVORITES, "$COL_NOMBRE = ? AND $COL_MARCA = ? AND $COL_EMAIL = ?", arrayOf(producto.nombre, producto.marca, userEmail))
    }

    fun obtenerFavoritos(): List<Producto> {
        val db = dbHelper.readableDatabase
        val items = mutableListOf<Producto>()
        db.query(
            TABLE_FAVORITES, null, "$COL_EMAIL = ?", arrayOf(userEmail), null, null, "$COL_ID DESC"
        ).use { c ->
            val idxNombre = c.getColumnIndexOrThrow(COL_NOMBRE)
            val idxMarca = c.getColumnIndexOrThrow(COL_MARCA)
            val idxPrecio = c.getColumnIndexOrThrow(COL_PRECIO)
            val idxImagen = c.getColumnIndexOrThrow(COL_IMAGEN)
            while (c.moveToNext()) {
                items.add(
                    Producto(
                        nombre = c.getString(idxNombre),
                        marca = c.getString(idxMarca),
                        precio = c.getDouble(idxPrecio),
                        imagen = c.getInt(idxImagen),
                        descripcion = "",
                        notas = emptyList(),
                        esFavorito = true
                    )
                )
            }
        }
        return items
    }

    fun esFavorito(nombre: String, marca: String): Boolean {
        val db = dbHelper.readableDatabase
        db.query(
            TABLE_FAVORITES, arrayOf(COL_ID), "$COL_NOMBRE = ? AND $COL_MARCA = ? AND $COL_EMAIL = ?", arrayOf(nombre, marca, userEmail), null, null, null
        ).use { c ->
            return c.moveToFirst()
        }
    }
}
