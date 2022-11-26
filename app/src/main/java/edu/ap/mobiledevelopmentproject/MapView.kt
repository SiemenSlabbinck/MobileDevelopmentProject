package edu.ap.mobiledevelopmentproject

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_map_view.*
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import java.io.File


class MapView : AppCompatActivity(), LocationListener {

    private lateinit var mMapView: MapView
    var distance = 0f
    private var toilets = ArrayList<Toilet>()
    var sqlHelper: SqlHelper? = null

    var myMarkers: ArrayList<Marker> = ArrayList()
    private lateinit var locationManager: LocationManager
    lateinit var crntLocation:Location

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val osmConfig = Configuration.getInstance()
        osmConfig.userAgentValue = packageName
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConfig.osmdroidBasePath = basePath
        val tileCache = File(osmConfig.osmdroidBasePath, "tile")
        osmConfig.osmdroidTileCache = tileCache
        setContentView(R.layout.activity_map_view)
        mMapView = findViewById(R.id.mapview)

        if (hasPermissions()) {
            loadData()
            initMap()
        }
        else {
            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION), 100)
        }

        var btnAddLocation = findViewById<Button>(R.id.btn_addLocation)
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

        btn_showList.setOnClickListener {
            finish()
        }

        btnAddLocation.setOnClickListener {
            val i = Intent(this, AddLocation::class.java)
            resultLauncher.launch(i)
        }
    }

    private fun loadData() {
        if (sqlHelper == null)
            sqlHelper = SqlHelper(this@MapView)
        this.toilets = sqlHelper!!.getToilets() as ArrayList<Toilet>
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        mMapView.overlayManager.clear()
        addCurrentLocationMarker(GeoPoint(location.latitude, location.longitude), "Huidige locatie")
        setCenter(GeoPoint(location.latitude, location.longitude), "Huidige locatie")
        crntLocation = location
        addToiletMarkers(this.toilets)
    }

    private fun hasPermissions(): Boolean {
    return  ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun initMap() {
        mMapView?.setTileSource(TileSourceFactory.DEFAULT_TILE_SOURCE)
        // add receiver to get location from tap
        val mReceive: MapEventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint): Boolean {
                Toast.makeText(baseContext, p.latitude.toString() + " - " + p.longitude, Toast.LENGTH_LONG).show()
                return false
            }

            override fun longPressHelper(p: GeoPoint): Boolean {
                return false
            }
        }
        mMapView?.overlays?.add(MapEventsOverlay(mReceive))

        mMapView?.controller?.setZoom(17.0)
        getLocation()
    }

    private fun addCurrentLocationMarker(geoPoint: GeoPoint, name: String) {
        val myMarker = Marker(mMapView)
        myMarker.position = GeoPoint(geoPoint.latitude, geoPoint.longitude)
        myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        myMarker.title = name
        myMarker.icon = resources.getDrawable(R.drawable.you_are_here)
        mMapView.getOverlays().add(myMarker)
        myMarkers.add(myMarker)
    }

    private fun addMarker(geoPoint: GeoPoint, name: String) {
        val myMarker = Marker(mMapView)
        myMarker.position = GeoPoint(geoPoint.latitude, geoPoint.longitude)
        myMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        myMarker.title = name
        myMarker.icon = resources.getDrawable(R.drawable.pin_location)
        myMarker.snippet = "${distanceBetween(geoPoint.latitude, geoPoint.longitude)} km"
        mMapView.getOverlays().add(myMarker)
        myMarkers.add(myMarker)
    }

    private fun setCenter(geoPoint: GeoPoint, name: String) {
        mMapView?.controller?.setCenter(geoPoint)
    }

    private fun addToiletMarkers(toilets:ArrayList<Toilet>) {
        if(toilets.size>0) {
            for (toilet in toilets) {
                addMarker(GeoPoint(toilet.x_coord!!, toilet.y_coord!!), toilet.street + " " + toilet.number)
            }
        } else {
            Toast.makeText(baseContext, "Geen toiletten gevonden", Toast.LENGTH_LONG).show()
        }
    }

    private fun distanceBetween(latitude:Double, longitude:Double): String? {

        val newLocation = Location("newlocation")
        newLocation.setLatitude(latitude);
        newLocation.setLongitude(longitude);

        distance = crntLocation.distanceTo(newLocation) / 1000; // in km
        var result = String.format("%.2f", distance);

        return result
    }




}