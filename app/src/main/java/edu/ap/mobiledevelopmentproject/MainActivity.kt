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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*var sqlHelper = SqlHelper(this@MainActivity)
        sqlHelper.fetchJson()*/
    }
}



