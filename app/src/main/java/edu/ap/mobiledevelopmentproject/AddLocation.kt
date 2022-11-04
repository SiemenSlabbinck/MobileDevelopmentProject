package edu.ap.mobiledevelopmentproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup

class AddLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        var available = findViewById<RadioGroup>(R.id.availableGroup)
        var btnSaveLocation = findViewById<Button>(R.id.btnSaveLocation)
        var btn_goBack = findViewById<Button>(R.id.btn_goBack)

        btnSaveLocation.setOnClickListener {
            var checkedId = available.checkedRadioButtonId;
            if(checkedId == -1) {
                // No radio buttons are checked
                Message.message(applicationContext, "Selecteer een optie")
            } else {
                // One of the options is checked
                findRadioButton(checkedId);
            }
        }

        //Clear values
        btn_goBack.setOnClickListener {
            available.clearCheck()
        }

    }

    private fun findRadioButton(checkedId: Int) {
        when (checkedId) {
            R.id.isAvailable -> return //add to JSON to save
        }
    }

}