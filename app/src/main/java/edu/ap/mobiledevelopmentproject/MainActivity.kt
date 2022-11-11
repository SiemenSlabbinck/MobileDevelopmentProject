package edu.ap.mobiledevelopmentproject

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import edu.ap.mobiledevelopmentproject.R.*

class MainActivity : AppCompatActivity() {

    var dataInitialized:Boolean = false
    var sqlHelper: SqlHelper? = null
    private var toilets = ArrayList<Toilet>()

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

    // Create data
    private fun createData() {
        //Create sqllite database
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        sqlHelper!!.fetchJson()

        //add sqllite data to firebase
        var firebaseHelper = FirebaseHelper()
        var toilets = sqlHelper!!.getToilets()
        if (toilets != null) {
            for (toilet in toilets){
                firebaseHelper.add(toilet)
            }
        }
    }

    // Load data
    private fun loadData() {
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        this.toilets = sqlHelper!!.getToilets() as ArrayList<Toilet>
    // Load data in listView
        loadDataInList(toilets)
    }

    fun loadDataInList(toilets:ArrayList<Toilet>) {
        val arrayAdapter: ArrayAdapter<*>
        val toiletFormattedList = ArrayList<String>()
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

    // Filters
    fun filterListOnGender(gender:String) {
        val toilets = sqlHelper!!.getToilets() as ArrayList<Toilet>
        val filteredList = ArrayList<Toilet>()
        filteredList.clear()

        for (e in toilets) {
            if(gender == "man") {
                for(toilet in toilets) {
                    if(toilet.target_audience == gender)
                        filteredList.add(toilet)
                }
            }
            if(gender == "vrouw") {
                for(toilet in toilets) {
                    if(toilet.target_audience == gender)
                        filteredList.add(toilet)
                }
            }
            if(gender == "man/vrouw") {
                for(toilet in toilets) {
                    if(toilet.target_audience == gender)
                        filteredList.add(toilet)
                }            }
        }

        loadDataInList(filteredList)
    }

    fun filterListOnWheelchairAvailable(wheelchairAvailable:Boolean) {
        val toilets = sqlHelper!!.getToilets() as ArrayList<Toilet>
        val filteredList = ArrayList<Toilet>()
        filteredList.clear()

        for (e in toilets) {
            if(wheelchairAvailable) {
                for(toilet in toilets) {
                    if(toilet.wheelchair_accessible == "ja")
                        filteredList.add(toilet)
                }
            }
            if(!wheelchairAvailable) {
                for(toilet in toilets) {
                    if(toilet.wheelchair_accessible == "nee" || toilet.wheelchair_accessible == null)
                        filteredList.add(toilet)
                }
            }
        }

        loadDataInList(filteredList)
    }

    fun filterListOnDamperTable(damperTable:String) {
        val toilets = sqlHelper!!.getToilets() as ArrayList<Toilet>
        val filteredList = ArrayList<Toilet>()
        filteredList.clear()

        for (e in toilets) {
            if(damperTable == "ja") {
                for(toilet in toilets) {
                    if(toilet.changing_table == damperTable)
                        filteredList.add(toilet)
                }
            }
            if(damperTable == "nee") {
                for(toilet in toilets) {
                    if(toilet.changing_table == damperTable || toilet.changing_table == null)
                        filteredList.add(toilet)
                }
            }
            if(damperTable == "niet van toepassing") {
                for(toilet in toilets) {
                    if(toilet.changing_table == damperTable || toilet.changing_table == null)
                        filteredList.add(toilet)
                }            }
        }

        loadDataInList(filteredList)
    }


    //    Popup methods
    private fun displayFilterDialog() {
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
                id.radio_option_m ->
                    if (checked) {
                        // show only man toilet
                        filterListOnGender("man")
                        showToast("Mannen toiletten zichtbaar")
                    }
                id.radio_option_v ->
                    if (checked) {
                        // show only woman toilet
                        filterListOnGender("vrouw")
                        showToast("Vrouwen toiletten zichtbaar")
                    }
                id.radio_option_mv ->
                    if (checked) {
                        // show man and women toilet
                        filterListOnGender("man/vrouw")
                        showToast("Alle toiletten zichtbaar")
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
                        filterListOnWheelchairAvailable(true)
                        showToast("Rolstoel vriendelijke toiletten zichtbaar")
                    }
                id.radio_option_wheelchairNo ->
                    if (checked) {
                        // show all toilets
                        filterListOnWheelchairAvailable(false)
                        showToast("Rolstoel onvriendelijke toiletten zichtbaar")
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
                        filterListOnDamperTable("ja")
                        showToast("Toiletten met luiertafel zichtbaar")
                    }
                id.radio_option_damperTableNotAvailable ->
                    if (checked) {
                        // show all toilets
                        filterListOnDamperTable("nee")
                        showToast("Toiletten zonder luiertafel zichtbaar")
                    }
                id.radio_option_damperTableNoToilet ->
                    if (checked) {
                        // show all toilets
                        filterListOnDamperTable("niet van toepassing")
                        showToast("Alle toiletten zichtbaar")
                    }
            }
        }
    }


    fun showToast(message: String) {
        Toast.makeText(baseContext, message.toString(), Toast.LENGTH_LONG).show()
    }
}



