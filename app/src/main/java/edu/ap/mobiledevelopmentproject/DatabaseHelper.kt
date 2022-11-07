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

    // adds toilet to sqllite database
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

    // deletes all toilets from database
    fun deleteToilet(){
        val db = this.writableDatabase
        db.execSQL(DELETE_FROM_TABLE)
    }

    // returns list of toilets from sqlite database
    @SuppressLint("Range")
    fun allToilets(): ArrayList<Toilet>{

        val toiletList = ArrayList<Toilet>()
        val db = this.readableDatabase

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
                var toiletInfo = Toilet()
                toiletInfo.street = cursor.getString(cursor.getColumnIndex(STREET))
                toiletInfo.number = cursor.getString(cursor.getColumnIndex(NUMBER))
                toiletInfo.postal_code = cursor.getInt(cursor.getColumnIndex(POSTAL_CODE))
                toiletInfo.district = cursor.getString(cursor.getColumnIndex(DISTRICT))
                toiletInfo.target_audience = cursor.getString(cursor.getColumnIndex(TARGET_AUDIENCE))
                toiletInfo.wheelchair_accessible = cursor.getString(cursor.getColumnIndex(WHEELCHAIR_ACCESSIBLE))
                toiletInfo.changing_table = cursor.getString(cursor.getColumnIndex(CHANGING_TABLE))
                toiletInfo.x_coord = cursor.getDouble(cursor.getColumnIndex((X_COORD)))
                toiletInfo.y_coord = cursor.getDouble(cursor.getColumnIndex((Y_COORD)))
                toiletList.add(toiletInfo)
            }
        }
        cursor.close()

        return toiletList
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

        private val DELETE_FROM_TABLE = "DELETE FROM $TABLE_TOILETS"

        private val SELECT_TOILETS = "SELECT * FROM $TABLE_TOILETS"
    }
}