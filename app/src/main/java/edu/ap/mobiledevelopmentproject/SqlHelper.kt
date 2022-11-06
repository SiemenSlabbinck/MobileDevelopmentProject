package edu.ap.mobiledevelopmentproject

import android.content.Context
import okhttp3.*
import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import com.beust.klaxon.Parser
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import androidx.appcompat.app.AppCompatActivity

class SqlHelper(_context: Context) {

    private var databaseHelper: DatabaseHelper? = null
    private var context = _context

    fun createSQL(resultArray: ArrayList<Feature>?){
        if (resultArray != null) {
            databaseHelper = DatabaseHelper(context);

            databaseHelper!!.deleteToilet()

            for (obj in resultArray){
                databaseHelper!!.addToilet(obj)
            }
        }

        println(databaseHelper!!.allToilets())

    }

    fun fetchJson(){
        println("Attempting to fetch JSON")
        val url = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?outFields=*&where=1%3D1&f=geojson"
        val request = Request.Builder().url(url).build()

        var resultArray: ArrayList<Feature>? = null

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                resultArray = (gson.fromJson(body, Toilet::class.java)).features
                createSQL(resultArray)
            }
        })
    }
}