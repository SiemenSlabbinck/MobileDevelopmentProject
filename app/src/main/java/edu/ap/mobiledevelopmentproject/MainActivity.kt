package edu.ap.mobiledevelopmentproject

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.RadioButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var openFilterDiaglog = findViewById<ImageButton>(R.id.btn_filter)

        openFilterDiaglog.setOnClickListener {
            displayFilterDialog();
        }

    }

    private fun displayFilterDialog() {
        var popupDialog = Dialog(this)
        popupDialog.setCancelable(false)
        popupDialog.setContentView(R.layout.popup)

        //components of view
        var closeButton = popupDialog.findViewById<ImageButton>(R.id.btn_close_popup)
        var filterButton = popupDialog.findViewById<Button>(R.id.btn_filter_on_values)

        //functions
        closeButton.setOnClickListener {
            //Dismiss popup dialog
            popupDialog.dismiss();
        }

        filterButton.setOnClickListener {
            //Dismiss popup dialog
            popupDialog.dismiss();
        }

        popupDialog.show();
    }

//    Popup methods
    fun onRadioButtonGenderClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_option_m ->
                    if (checked) {
                        // show only man toilet
                    }
                R.id.radio_option_v ->
                    if (checked) {
                        // show only woman toilet
                    }
                R.id.radio_option_mv ->
                    if (checked) {
                        // show man and women toilet
                    }
            }
        }
    }

    fun onRadioButtonWheelchairClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_option_wheelchairYes ->
                    if (checked) {
                        // show only wheelchair friendly toilets
                    }
                R.id.radio_option_wheelchairNo ->
                    if (checked) {
                        // show all toilets
                    }
            }
        }
    }
}