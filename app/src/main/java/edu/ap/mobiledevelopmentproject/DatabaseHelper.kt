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

        values.put(STREET, jsonObject.properties?.STRAAT)
        values.put(NUMBER, jsonObject.properties?.HUISNUMMER)
        values.put(POSTAL_CODE, jsonObject.properties?.POSTCODE)
        values.put(DISTRICT, jsonObject.properties?.DISTRICT)
        values.put(TARGET_AUDIENCE, jsonObject.properties?.DOELGROEP)
        values.put(WHEELCHAIR_ACCESSIBLE, jsonObject.properties?.INTEGRAAL_TOEGANKELIJK)
        values.put(CHANGING_TABLE, jsonObject.properties?.LUIERTAFEL)
        values.put(X_COORD, jsonObject.geometry?.coordinates?.get(0))
        values.put(Y_COORD, jsonObject.geometry?.coordinates?.get(1))

        return db.insert(TABLE_TOILETS, null, values)
    }

    fun dropToilet(){
        val db = this.writableDatabase
        db.execSQL(DELETE_TABLE_TOILETS)
        db.close()
    }

    @SuppressLint("Range")
    fun allToilets(): ArrayList<String>{
        val toiletsArrayList = ArrayList<String>()
        var name: String
        val db = this.readableDatabase
        //val cursor = db.rawQuerry(SELECT_STUDENTS, null)

        val projection= arrayOf(KEY_ID, STREET, NUMBER, POSTAL_CODE, DISTRICT, TARGET_AUDIENCE, WHEELCHAIR_ACCESSIBLE, CHANGING_TABLE, X_COORD, Y_COORD)
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
                val street = cursor.getString(cursor.getColumnIndex(STREET))
                val number = cursor.getString(cursor.getColumnIndex(NUMBER))
                val postalCode = cursor.getString(cursor.getColumnIndex(POSTAL_CODE))
                val district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                val targetAudience = cursor.getString(cursor.getColumnIndex(TARGET_AUDIENCE))
                val wheelchairAccessible = cursor.getString(cursor.getColumnIndex(WHEELCHAIR_ACCESSIBLE))
                val changingTable = cursor.getString(cursor.getColumnIndex(CHANGING_TABLE))
                val xCoord = cursor.getString(cursor.getColumnIndex((X_COORD)))
                val yCoord = cursor.getString(cursor.getColumnIndex((Y_COORD)))
                toiletsArrayList.add("$street $number $postalCode $district $targetAudience $wheelchairAccessible $changingTable $xCoord $yCoord")
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
        private val STREET = "street"
        private val NUMBER = "number"
        private val POSTAL_CODE = "postal_code"
        private val DISTRICT = "disctrict"
        private val TARGET_AUDIENCE = "target_audience"
        private val WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible"
        private val CHANGING_TABLE = "changing_table"
        private val X_COORD = "x_coord"
        private val Y_COORD = "y_coord"

        private val CREATE_TABLE_TOILETS = ("CREATE TABLE "
                + TABLE_TOILETS + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + STREET + " TEXT,"
                + NUMBER + " INTEGER,"
                + POSTAL_CODE + " TEXT,"
                + DISTRICT + " TEXT,"
                + TARGET_AUDIENCE + " TEXT,"
                + WHEELCHAIR_ACCESSIBLE + " TEXT,"
                + CHANGING_TABLE + " TEXT,"
                + X_COORD + " TEXT,"
                + Y_COORD + " TEXT );")

        private val DELETE_TABLE_TOILETS = "DROP TABLE IF EXISTS $TABLE_TOILETS"

        private val SELECT_TOILETS = "SELECT * FROM $TABLE_TOILETS"
    }
}