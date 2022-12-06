package edu.ap.mobiledevelopmentproject

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import edu.ap.mobiledevelopmentproject.R.id
import edu.ap.mobiledevelopmentproject.R.layout

class MainActivity : AppCompatActivity(), LocationListener {

    var dataInitialized:Boolean = false
    var sqlHelper: SqlHelper? = null
    private var toilets = ArrayList<Toilet>()
    private lateinit var locationManager: LocationManager
    lateinit var crntLocation:Location
    var distance = 0f
    private var toiletFormattedList = ArrayList<String>()
    private var gender: String? = null
    private var wheelchair: String? = null
    private var changingTable: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_main)

        var openFilterDiaglog = findViewById<ImageButton>(id.btn_filter)
        var btnShowMapView = findViewById<Button>(id.btn_showMap)
        var btnAddLocation = findViewById<Button>(id.btn_addLocation)

        val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
            }
        }

        val requestMultiplePermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result ->
                if(result[Manifest.permission.ACCESS_FINE_LOCATION] != true || result[Manifest.permission.ACCESS_COARSE_LOCATION] != true)
                {
                    val i = Intent(this, PermissionDenied::class.java)
                    resultLauncher.launch(i)
                } else {
                    getLocation()
                    loadData()
                }
            }

        requestMultiplePermissionLauncher.launch(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        )

        if(dataInitialized)
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

    override fun onResume() {
        super.onResume()
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this)
        this.toilets = sqlHelper!!.getToilets(null) as ArrayList<Toilet>
        loadDataInList(toilets)
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        try {
            crntLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        } catch (ex: Exception){
            Log.d("location", ex.toString())
        }
    }

    override fun onLocationChanged(location: Location) {
        crntLocation = location
        loadDataInList(toilets)
    }

    //region Create/Load data
    private fun createData() {
        //Get data from firestore and save in sqlite
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        val firebaseHelper = FirebaseHelper(this@MainActivity)
        firebaseHelper.read()
    }

    private fun loadData() {
        //Load data from sqlite
        Log.w(ContentValues.TAG, "Loading data")
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MainActivity)
        this.toilets = sqlHelper!!.getToilets(null) as ArrayList<Toilet>
        if (toilets.size == 0){
            createData()
            Message.message(baseContext, "Open kaart weergave om de toiletten te laden")
        }
        // Load data in listView
        loadDataInList(toilets)
    }

    fun loadDataInList(toilets:ArrayList<Toilet>) {
        toiletFormattedList.clear()
        val arrayAdapter: ArrayAdapter<*>
        if (toilets != null) {
            for (toilet in toilets) {
                toiletFormattedList.add(toilet.street.toString() + " " + toilet.number.toString() + " - Afstand: " + distanceBetween(
                    toilet.x_coord!!, toilet.y_coord!!) + " km")
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

    //region popup methods
    private fun displayFilterDialog() {
        var popupDialog = Dialog(this)
        popupDialog.setCancelable(false)
        popupDialog.setContentView(layout.popup)

        //components of view
        var closeButton = popupDialog.findViewById<ImageButton>(id.btn_close_popup)
        var filterButton = popupDialog.findViewById<Button>(id.btn_filter)
        var clearFilterButton = popupDialog.findViewById<Button>(id.clear_filter)

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

        clearFilterButton.setOnClickListener {
            this.toilets = sqlHelper!!.getToilets(null) as ArrayList<Toilet>
            loadDataInList(toilets)
            popupDialog.dismiss();
        }
        popupDialog.show();
    }

    fun filter(){
        var filteredList = ArrayList<Toilet>()
        filteredList.clear()
        var que: String? = ""
        if (gender != null)
            que += "target_audience = '$gender' OR target_audience = 'man/vrouw'"
        if (wheelchair != null) {
            if (que != null)
                if (que.isNotBlank())
                    que += " AND "
            que += "wheelchair_accessible = '$wheelchair'"
            if (wheelchair == "nee")
                que += "OR wheelchair_accessible IS NULL"
        }
        if (changingTable != null) {
            if (que != null) {
                if (que.isNotBlank())
                    que += " AND "
            }
            que += "changing_table = '$changingTable'"
            if (changingTable == "nee")
                que += "OR changing_table IS NULL"
        }

        filteredList = sqlHelper!!.getToilets(que) as ArrayList<Toilet>
        gender = null
        wheelchair = null
        changingTable = null
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
                        changingTable = "Aanwezig"
                    }
                id.radio_option_damperTableNotAvailable ->
                    if (checked) {
                        changingTable = "nee"
                    }
                id.radio_option_damperTableNoToilet ->
                    if (checked) {
                        changingTable = "niet van toepassing"
                    }
            }
        }
    }
    //endregion

    private fun distanceBetween(latitude:Double, longitude:Double): String? {

        val newLocation = Location("newlocation")
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);
        try {
            distance = crntLocation.distanceTo(newLocation) / 1000; // in km
            var result = String.format("%.2f", distance);

            return result
        } catch (ex: Exception) {
            return null
        }
    }
}



