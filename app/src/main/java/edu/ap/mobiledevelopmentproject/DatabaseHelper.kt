package edu.ap.mobiledevelopmentproject

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_TOILETS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DELETE_TABLE_TOILETS)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }

    companion object {

        var DATABASE_NAME = "toilet_database"
        private val DATABASE_VERSION = 1
        private val TABLE_TOILETS = "toilets"
        private val KEY_ID = "id"
        private val STRAAT = "straat"
        private val HUISNUMMER = "huisnummer"
        private val POSTCODE = "postcode"
        private val DISTRICT = "disctrict"
        private val DOELGROEP = "doelgroep"
        private val INTEGRAAL_TOEGANKELIJK = "integraal_toegankelijk"
        private val LUIERTAFEL = "luiertafel"
        private val X_COORD = "lat"
        private val Y_COORD = "long"

        private val CREATE_TABLE_TOILETS = ("CREATE TABLE "
                + TABLE_TOILETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STRAAT + " TEXT,"
                + HUISNUMMER + " INTEGER,"
                + POSTCODE + " TEXT,"
                + DISTRICT + " TEXT,"
                + DOELGROEP + " TEXT,"
                + INTEGRAAL_TOEGANKELIJK + " TEXT,"
                + LUIERTAFEL + " TEXT,"
                + X_COORD + " TEXT,"
                + Y_COORD + " TEXT );")

        private val DELETE_TABLE_TOILETS = "DROP TABLE IF EXISTS $TABLE_TOILETS"

        private val SELECT_TOILETS = "SELECT * FROM $TABLE_TOILETS"
    }
}