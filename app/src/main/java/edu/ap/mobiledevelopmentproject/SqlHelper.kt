package edu.ap.mobiledevelopmentproject

import android.content.Context

class SqlHelper(_context: Context) {

    private var databaseHelper: DatabaseHelper? = null
    private var context = _context

    fun createSQL(toiletArray: ArrayList<Toilet>){
        databaseHelper = DatabaseHelper(context);
        for (toilet in toiletArray)
            databaseHelper!!.addToilet(toilet)
    }

    // returns list of toilets from sqlite database
    fun getToilets(): java.util.ArrayList<Toilet>? {
        databaseHelper = DatabaseHelper(context);
        var toiletList = databaseHelper?.allToilets()
        return toiletList
    }
}