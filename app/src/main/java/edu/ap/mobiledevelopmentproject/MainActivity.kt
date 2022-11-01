package edu.ap.mobiledevelopmentproject

import android.os.Bundle
import android.provider.BaseColumns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Parser
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var databaseHelper: DatabaseHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchJson()
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

    fun createSQL(resultArray: ArrayList<Feature>?) {
        if (resultArray != null) {
            databaseHelper = DatabaseHelper(this);
            //databaseHelper!!.dropToilet()
            for (obj in resultArray){
                databaseHelper!!.addToilet(obj)
            }
        }

        println(databaseHelper!!.allToilets())

    }
}



