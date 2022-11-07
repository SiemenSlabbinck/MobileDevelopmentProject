package edu.ap.mobiledevelopmentproject

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupWindow
import android.widget.RadioButton
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {

    var dataInitialized:Boolean = false
    var sqlHelper: SqlHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var openFilterDiaglog = findViewById<ImageButton>(R.id.btn_filter)
        var btnShowMapView = findViewById<Button>(R.id.btn_showMap)
        var btnAddLocation = findViewById<Button>(R.id.btn_addLocation)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if(data != null)
                {
                    //If data does not exist yet -> create
                    createData();
                }

            }
        }

        loadData()

        if(dataInitialized == true)
        {
            val i = Intent(this, MapView::class.java)
            resultLauncher.launch(i)
        }

        //Buttons clicked functions
        openFilterDiaglog.setOnClickListener {
            displayFilterDialog();
        }

        btnShowMapView.setOnClickListener {
            val i = Intent(this, MapView::class.java)
            resultLauncher.launch(i)
        }

        btnAddLocation.setOnClickListener {
            val i = Intent(this, AddLocation::class.java)
            resultLauncher.launch(i)
        }
    }

    // Create data
    private fun createData() {
        /*
        //Fetch json and create sqllite database
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        var json = sqlHelper.fetchJson()
        if (json != null)
            sqlHelper.createSQL(json)

        //add sqllite data to firebase
        var firebaseHelper = FirebaseHelper()
        var toilets = sqlHelper.getToilets()
        if (toilets != null) {
            for (toilet in toilets){
                firebaseHelper.add(toilet)
            }
        }
         */
    }

    // Load data
    private fun loadData() {
        /*
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        var toilets = sqlHelper!!.getToilets()
         */
    }

    //    Popup methods
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



