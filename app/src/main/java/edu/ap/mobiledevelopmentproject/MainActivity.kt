package edu.ap.mobiledevelopmentproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.JsonReader
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import org.json.JSONObject
import java.io.StringReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    private var databaseHelper: DatabaseHelper? = null
    private val parser: Parser = Parser.default()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this);
        val apiResponse = URL("https://geodata.antwerpen.be/arcgissql/rest/services/P_Portal/portal_publiek1/MapServer/8/query?where=1%3D1&outFields=*&outSR=4326&f=json").readText()
        val arr = parser.parse(apiResponse) as JsonArray<JSONObject>
        for (obj in arr){
            databaseHelper!!.addToilet(obj)
        }
    }
}