package edu.ap.mobiledevelopmentproject

import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import edu.ap.mobiledevelopmentproject.R.id
import edu.ap.mobiledevelopmentproject.R.layout

class MainActivity : AppCompatActivity() {

    var dataInitialized:Boolean = false
    var sqlHelper: SqlHelper? = null
    private var toilets = ArrayList<Toilet>()
    private var toiletFormattedList = ArrayList<String>()
    private var gender: String? = null
    private var wheelchair: String? = null
    private var damper_Table: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        var openFilterDiaglog = findViewById<ImageButton>(id.btn_filter)
        var btnShowMapView = findViewById<Button>(id.btn_showMap)
        var btnAddLocation = findViewById<Button>(id.btn_addLocation)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data

                if(data != null)
                {
                    //If data does not exist yet -> create
                    //createData();
                }

            }
        }
       // createData();

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

    //region Create/Load data
    private fun createData() {
        //Get data from firestore and save in sqlite
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        var firebaseHelper = FirebaseHelper(this@MainActivity)
        var toilets = firebaseHelper.read()
        if (toilets != null) {
            for (toilet in toilets){
                sqlHelper!!.createSQL(toilets)
            }
        }
    }

    private fun loadData() {
        //Load data from sqlite
        Log.w(ContentValues.TAG, "Loading data")
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        this.toilets = sqlHelper!!.getToilets(null) as ArrayList<Toilet>
        if (toilets.size == 0){
            createData()
        }
    // Load data in listView
        loadDataInList(toilets)
    }

    fun loadDataInList(toilets:ArrayList<Toilet>) {
        toiletFormattedList.clear()
        val arrayAdapter: ArrayAdapter<*>
        if (toilets != null) {
            for (toilet in toilets) {
                toiletFormattedList.add(toilet.street.toString() + " " + toilet.number.toString())
            }
        }
        if(toilets.isEmpty())
            toiletFormattedList.add("Geen toiletten gevonden...")

        // access the listView from xml file
        var mListView = findViewById<ListView>(id.listView)
        arrayAdapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, toiletFormattedList)
        mListView.adapter = arrayAdapter

    }
    //endregion


    //region popup methods
    private fun displayFilterDialog() {
        toiletFormattedList.clear()
        var popupDialog = Dialog(this)
        popupDialog.setCancelable(false)
        popupDialog.setContentView(layout.popup)

        //components of view
        var closeButton = popupDialog.findViewById<ImageButton>(id.btn_close_popup)
        var filterButton = popupDialog.findViewById<Button>(id.btn_filter_on_values)

        //functions
        closeButton.setOnClickListener {
            //Dismiss popup dialog
            popupDialog.dismiss();
        }

        filterButton.setOnClickListener {
            //Dismiss popup dialog
            filter()
            popupDialog.dismiss();
        }

        popupDialog.show();
    }

    fun filter(){
        var filteredList = ArrayList<Toilet>()
        filteredList.clear()
        var que: String? = ""
        if (gender != null)
            que += "target_audience = '$gender'"
        if (wheelchair != null) {
            if (que != null)
                if (que.isNotBlank())
                    que += " AND "
            que += "wheelchair_accessible = '$wheelchair'"
        }
        if (damper_Table != null) {
            if (que != null) {
                if (que.isNotBlank())
                    que += " AND "
            }
            que += "changing_table = '$damper_Table'"
        }

        filteredList = sqlHelper!!.getToilets(que) as ArrayList<Toilet>
        gender = null
        wheelchair = null
        damper_Table = null
        loadDataInList(filteredList)
    }

    fun onRadioButtonGenderClicked(view: View) {

        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                id.radio_option_m ->
                    if (checked) {
                        // show only man toilet
                        gender = "man"
                    }
                id.radio_option_v ->
                    if (checked) {
                        // show only woman toilet
                        gender = "vrouw"
                    }
                id.radio_option_mv ->
                    if (checked) {
                        // show man and women toilet
                        gender = "man/vrouw"
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
                id.radio_option_wheelchairYes ->
                    if (checked) {
                        // show only wheelchair friendly toilets
                        wheelchair = "ja"
                    }
                id.radio_option_wheelchairNo ->
                    if (checked) {
                        // show all toilets
                        wheelchair = "nee"
                    }
            }
        }
    }

    fun onRadioButtonDamperTableClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                id.radio_option_damperTableAvailable ->
                    if (checked) {
                        // show only wheelchair friendly toilets
                        damper_Table = "ja"
                    }
                id.radio_option_damperTableNotAvailable ->
                    if (checked) {
                        // show all toilets
                        damper_Table = "nee"
                    }
                id.radio_option_damperTableNoToilet ->
                    if (checked) {
                        // show all toilets
                        damper_Table = "niet van toepassing"
                    }
            }
        }
    }
    //endregion


    fun showToast(message: String) {
        Toast.makeText(baseContext, message.toString(), Toast.LENGTH_LONG).show()
    }
}



