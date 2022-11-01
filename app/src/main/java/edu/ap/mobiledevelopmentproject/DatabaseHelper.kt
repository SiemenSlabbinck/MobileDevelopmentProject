package edu.ap.mobiledevelopmentproject

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.ArrayList


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

    fun addToilet(jsonObject: Feature): Long {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(STRAAT, jsonObject.properties?.STRAAT)
        values.put(HUISNUMMER, jsonObject.properties?.HUISNUMMER)
        values.put(POSTCODE, jsonObject.properties?.POSTCODE)
        values.put(DISTRICT, jsonObject.properties?.DISTRICT)
        values.put(DOELGROEP, jsonObject.properties?.DOELGROEP)
        values.put(INTEGRAAL_TOEGANKELIJK, jsonObject.properties?.INTEGRAAL_TOEGANKELIJK)
        values.put(LUIERTAFEL, jsonObject.properties?.LUIERTAFEL)
        values.put(X_COORD, jsonObject.geometry?.coordinates?.get(0))
        values.put(Y_COORD, jsonObject.geometry?.coordinates?.get(1))

        return db.insert(TABLE_TOILETS, null, values)
    }

    @SuppressLint("Range")
    fun allToilets(): ArrayList<String>{
        val toiletsArrayList = ArrayList<String>()
        var name: String
        val db = this.readableDatabase
        //val cursor = db.rawQuerry(SELECT_STUDENTS, null)

        val projection= arrayOf(KEY_ID, STRAAT, HUISNUMMER, POSTCODE, DISTRICT, DOELGROEP, INTEGRAAL_TOEGANKELIJK, LUIERTAFEL, X_COORD, Y_COORD)
        val sortOrder = "${KEY_ID} ASC"

        val cursor = db.query(
            TABLE_TOILETS,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )

        with(cursor) {
            while (moveToNext()) {
                val straat = cursor.getString(cursor.getColumnIndex(STRAAT))
                val huisnummer = cursor.getString(cursor.getColumnIndex(HUISNUMMER))
                val postcode = cursor.getString(cursor.getColumnIndex(POSTCODE))
                val district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                val doelgroep = cursor.getString(cursor.getColumnIndex(DOELGROEP))
                val integraalToegankelijk = cursor.getString(cursor.getColumnIndex(INTEGRAAL_TOEGANKELIJK))
                val luiertafel = cursor.getString(cursor.getColumnIndex(LUIERTAFEL))
                val xCoord = cursor.getString(cursor.getColumnIndex((X_COORD)))
                val yCoord = cursor.getString(cursor.getColumnIndex((Y_COORD)))
                toiletsArrayList.add("$straat $huisnummer $postcode $district $doelgroep $integraalToegankelijk $luiertafel $xCoord $yCoord")
            }
        }
        cursor.close()

        return toiletsArrayList
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