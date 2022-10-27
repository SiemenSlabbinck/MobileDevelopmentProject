package edu.ap.mobiledevelopmentproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.beust.klaxon.Parser
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private var databaseHelper: DatabaseHelper? = null
    private val parser: Parser = Parser.default()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchJson()

        //databaseHelper = DatabaseHelper(this);

        // arr = parser.parse(apiResponse) as JsonArray<JSONObject>
        //for (obj in arr){
        //    databaseHelper!!.addToilet(obj)
        //}
    }

    fun fetchJson(){
        println("Attempting to fetch JSON")
        val url = "https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?outFields=*&where=1%3D1&f=geojson"

        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }

            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                println(body)

                val gson = GsonBuilder().create()

                val toilet = (gson.fromJson(body, Toilet::class.java)).features
            }
        })

    }
}

class Feature {
    var geometry: Geometry? = null
    var properties: Properties? = null
}

class Geometry {
    var coordinates: ArrayList<Double>? = null
}

class Properties {
    var OMSCHRIJVING: String? = null

    var TYPE: String? = null

    var BETALEND: String? = null

    var STRAAT: String? = null

    var HUISNUMMER: String? = null

    var POSTCODE = 0

    var DISTRICT: String? = null

    var DOELGROEP: String? = null

    var INTEGRAAL_TOEGANKELIJK: String? = null

    var LUIERTAFEL: String? = null

    var X_COORD = 0.0

    var Y_COORD = 0.0
}

class Toilet {
    var features: ArrayList<Feature>? = null
}