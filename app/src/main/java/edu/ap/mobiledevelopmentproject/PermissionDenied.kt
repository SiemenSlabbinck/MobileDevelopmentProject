package edu.ap.mobiledevelopmentproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_map_view.*

class PermissionDenied : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_permission_denied)
        btn_showList.setOnClickListener {
            finish()
        }
    }
}