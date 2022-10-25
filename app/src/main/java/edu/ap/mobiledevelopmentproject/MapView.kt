package edu.ap.mobiledevelopmentproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MapView : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_view)
    }

    fun goBack(view:View) {
        finish()
    }


    override fun finish(): Unit {
        super.finish()
    }
}