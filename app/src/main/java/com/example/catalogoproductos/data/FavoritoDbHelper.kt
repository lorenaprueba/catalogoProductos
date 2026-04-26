package com.example.catalogoproductos.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FavoritoDbHelper(context: Context) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME     = "favoritos.db"
        const val DB_VERSION  = 1
        const val TABLE       = "favoritos"
        const val COL_ID      = "id"
        const val COL_PROD_ID = "producto_id"
        const val COL_NOMBRE  = "nombre"
        const val COL_MARCA   = "marca"
        const val COL_PRECIO  = "precio"
        const val COL_IMAGEN  = "imagen"
        const val COL_DESC    = "descripcion"
        const val COL_FECHA   = "fecha"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE (
                $COL_ID      INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_PROD_ID TEXT    UNIQUE NOT NULL,
                $COL_NOMBRE  TEXT    NOT NULL,
                $COL_MARCA   TEXT    NOT NULL,
                $COL_PRECIO  REAL    NOT NULL,
                $COL_IMAGEN  INTEGER DEFAULT 0,
                $COL_DESC    TEXT    DEFAULT '',
                $COL_FECHA   INTEGER NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }
}