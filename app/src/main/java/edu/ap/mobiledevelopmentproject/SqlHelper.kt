package edu.ap.mobiledevelopmentproject

import android.content.Context

class SqlHelper(_context: Context) {

    private var databaseHelper: DatabaseHelper? = null
    private var context = _context

    fun createSQL(toiletArray: ArrayList<Toilet>){
        if (databaseHelper == null)
            databaseHelper = DatabaseHelper(context);
        for (toilet in toiletArray)
            databaseHelper!!.addToilet(toilet)
    }

    fun addToilet(toilet: Toilet){
        if (databaseHelper == null)
            databaseHelper = DatabaseHelper(context);
        databaseHelper!!.addToilet(toilet)

    }

    // returns list of toilets from sqlite database
    fun getToilets(selection: String?): java.util.ArrayList<Toilet>? {
        if (databaseHelper == null)
            databaseHelper = DatabaseHelper(context);
        var toiletList = databaseHelper?.allToilets(selection)
        return toiletList
    }
}