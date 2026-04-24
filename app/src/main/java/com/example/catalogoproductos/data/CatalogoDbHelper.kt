package com.example.catalogoproductos.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class CatalogoDbHelper(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_CART (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_NOMBRE TEXT NOT NULL,
                $COL_MARCA TEXT NOT NULL,
                $COL_PRECIO REAL NOT NULL,
                $COL_IMAGEN INTEGER NOT NULL,
                $COL_CANTIDAD INTEGER NOT NULL,
                UNIQUE($COL_NOMBRE, $COL_MARCA)
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_ORDERS (
                $COL_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_ORDER_FECHA INTEGER NOT NULL,
                $COL_ORDER_TOTAL REAL NOT NULL,
                $COL_ORDER_ITEMS_COUNT INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_ORDER_ITEMS (
                $COL_OI_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_OI_ORDER_ID INTEGER NOT NULL,
                $COL_OI_NOMBRE TEXT NOT NULL,
                $COL_OI_MARCA TEXT NOT NULL,
                $COL_OI_PRECIO REAL NOT NULL,
                $COL_OI_IMAGEN INTEGER NOT NULL DEFAULT 0,
                $COL_OI_CANTIDAD INTEGER NOT NULL,
                FOREIGN KEY($COL_OI_ORDER_ID) REFERENCES $TABLE_ORDERS($COL_ORDER_ID) ON DELETE CASCADE
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER_ITEMS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_ORDERS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    companion object {
        const val DB_NAME = "catalogo.db"
        const val DB_VERSION = 2

        const val TABLE_CART = "cart_items"
        const val COL_ID = "id"
        const val COL_NOMBRE = "nombre"
        const val COL_MARCA = "marca"
        const val COL_PRECIO = "precio"
        const val COL_IMAGEN = "imagen"
        const val COL_CANTIDAD = "cantidad"

        const val TABLE_ORDERS = "orders"
        const val COL_ORDER_ID = "id"
        const val COL_ORDER_FECHA = "fecha"
        const val COL_ORDER_TOTAL = "total"
        const val COL_ORDER_ITEMS_COUNT = "items_count"

        const val TABLE_ORDER_ITEMS = "order_items"
        const val COL_OI_ID = "id"
        const val COL_OI_ORDER_ID = "order_id"
        const val COL_OI_NOMBRE = "nombre"
        const val COL_OI_MARCA = "marca"
        const val COL_OI_PRECIO = "precio"
        const val COL_OI_IMAGEN = "imagen"
        const val COL_OI_CANTIDAD = "cantidad"
    }
}
