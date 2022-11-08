package edu.ap.mobiledevelopmentproject

import android.content.Context
import okhttp3.*
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.widget.Toast
import com.beust.klaxon.Parser
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity
import java.util.ArrayList

class SqlHelper(_context: Context) {

    private var databaseHelper: DatabaseHelper? = null
    private var context = _context

    // creates sqlite database from json string
    fun createSQL(resultString: String?){

        println("Attempting to create SQL")
        val gson = GsonBuilder().create()
        var toiletArray = (gson.fromJson(resultString, ToiletGson::class.java)).features

        if (toiletArray != null) {
            databaseHelper = DatabaseHelper(context);
            databaseHelper!!.deleteToilet()

            for (obj in toiletArray){
                databaseHelper!!.addToilet(obj)
            }
        }

        println(databaseHelper!!.allToilets())
    }

    fun createSQL(toiletArray: ArrayList<Toilet>){
        for (toilet in toiletArray)
            databaseHelper!!.addToilet(toilet)
    }

    // fetches json string from url
    fun fetchJson() {
        println("Attempting to fetch JSON")
        val url = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?outFields=*&where=1%3D1&f=geojson"
        val request = Request.Builder().url(url).build()

        var resultString: String? = null

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                resultString = response.body?.string()
                createSQL(resultString)

            }
        })
    }

    // returns list of toilets from sqlite database
    fun getToilets(): java.util.ArrayList<Toilet>? {
        databaseHelper = DatabaseHelper(context);
        var toiletList = databaseHelper?.allToilets()
        return toiletList
    }


}