package com.example.catalogoproductos.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.catalogoproductos.models.Producto
class FavoritoDao(context: Context) {

    private val helper = FavoritoDbHelper(context)

    /* ── Agregar ─────────────────────────────────────────────────── */
    fun agregar(p: Producto): Boolean {
        val db = helper.writableDatabase
        return try {
            val cv = ContentValues().apply {
                put(FavoritoDbHelper.COL_PROD_ID, p.id.toString())
                put(FavoritoDbHelper.COL_NOMBRE,  p.nombre)
                put(FavoritoDbHelper.COL_MARCA,   p.marca)
                put(FavoritoDbHelper.COL_PRECIO,  p.precio)
                put(FavoritoDbHelper.COL_IMAGEN,  p.imagen)           // Int directo
                put(FavoritoDbHelper.COL_DESC,    p.descripcion ?: "")
                put(FavoritoDbHelper.COL_FECHA,   System.currentTimeMillis())
                // notas NO se guarda — se reconstruye como emptyList() al leer
            }
            db.insertWithOnConflict(
                FavoritoDbHelper.TABLE, null, cv,
                SQLiteDatabase.CONFLICT_IGNORE
            ) != -1L
        } finally { db.close() }
    }

    /* ── Eliminar ────────────────────────────────────────────────── */
    fun eliminar(productoId: String): Boolean {
        val db = helper.writableDatabase
        return try {
            db.delete(
                FavoritoDbHelper.TABLE,
                "${FavoritoDbHelper.COL_PROD_ID} = ?",
                arrayOf(productoId)
            ) > 0
        } finally { db.close() }
    }

    /* ── ¿Es favorito? ───────────────────────────────────────────── */
    fun esFavorito(productoId: String): Boolean {
        val db = helper.readableDatabase
        return try {
            val c = db.query(
                FavoritoDbHelper.TABLE,
                arrayOf(FavoritoDbHelper.COL_ID),
                "${FavoritoDbHelper.COL_PROD_ID} = ?",
                arrayOf(productoId), null, null, null
            )
            val existe = c.count > 0
            c.close()
            existe
        } finally { db.close() }
    }

    /* ── Todos los favoritos ─────────────────────────────────────── */
    fun obtenerTodos(): List<Producto> {
        val db   = helper.readableDatabase
        val list = mutableListOf<Producto>()
        return try {
            val c = db.query(
                FavoritoDbHelper.TABLE,
                null, null, null, null, null,
                "${FavoritoDbHelper.COL_FECHA} DESC"
            )
            while (c.moveToNext()) {
                list += Producto(
                    id          = c.getString(
                        c.getColumnIndexOrThrow(FavoritoDbHelper.COL_PROD_ID)
                    ).toIntOrNull() ?: 0,
                    nombre      = c.getString(c.getColumnIndexOrThrow(FavoritoDbHelper.COL_NOMBRE)),
                    marca       = c.getString(c.getColumnIndexOrThrow(FavoritoDbHelper.COL_MARCA)),
                    precio      = c.getDouble(c.getColumnIndexOrThrow(FavoritoDbHelper.COL_PRECIO)),
                    imagen      = c.getInt(c.getColumnIndexOrThrow(FavoritoDbHelper.COL_IMAGEN)),
                    descripcion = c.getString(c.getColumnIndexOrThrow(FavoritoDbHelper.COL_DESC)),
                    notas       = emptyList()
                )
            }
            c.close()
            list
        } finally { db.close() }
    }

    /* ── Toggle ──────────────────────────────────────────────────── */
    fun toggle(p: Producto): Boolean {
        return if (esFavorito(p.id.toString())) {
            eliminar(p.id.toString())
            false
        } else {
            agregar(p)
            true
        }
    }
}
