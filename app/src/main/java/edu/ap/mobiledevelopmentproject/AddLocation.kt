package edu.ap.mobiledevelopmentproject

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.util.GeoPoint
import java.io.IOException


class AddLocation : AppCompatActivity() {
    private var sqlHelper: SqlHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)
        var btnSaveLocation = findViewById<Button>(R.id.btnSaveLocation)
        var btn_goBack = findViewById<Button>(R.id.btn_goBack)


        btnSaveLocation.setOnClickListener {
            getFormValue()
        }

        //Clear values
        btn_goBack.setOnClickListener {
            finish()
        }
    }

    private fun getFormValue() {
        var streetname = findViewById<EditText>(R.id.streetname)
        var housenumber = findViewById<EditText>(R.id.housenumber)
        var zipcode = findViewById<EditText>(R.id.zipcode)
        var district = findViewById<EditText>(R.id.district)
        var email = findViewById<EditText>(R.id.email)
        var latitude = ""
        var longitude = ""

        // radio's
        var genderGroup = findViewById<RadioGroup>(R.id.genderGroup);
        var wheelchairGroup = findViewById<RadioGroup>(R.id.wheelchairGroup);
        var damperTableGroup = findViewById<RadioGroup>(R.id.damperTableGroup);

        val selectedGenderId: Int = genderGroup.checkedRadioButtonId
        val selectedWheelchairId: Int = wheelchairGroup.checkedRadioButtonId
        val selectedDamperTableId: Int = damperTableGroup.checkedRadioButtonId

        var gender = findViewById<RadioButton>(selectedGenderId).text;
        var wheelchair = findViewById<RadioButton>(selectedWheelchairId).text;
        var damperTable = findViewById<RadioButton>(selectedDamperTableId).text;

        var address = "${streetname.text} ${housenumber.text}, ${zipcode.text} ${district.text}"
        var location = getLocationFromAddress(address)
        if (location != null) {
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
        }

//        TODO email van toevoeger nog aan toe voegen -> database changes?
        val toilet = Toilet(
            street = streetname.text.toString(),
            number = housenumber.text.toString(),
            postal_code = Integer.parseInt(zipcode.text.toString()),
            district = district.text.toString(),
            target_audience = gender.toString(),
            wheelchair_accessible = wheelchair.toString(),
            changing_table = damperTable.toString(),
            x_coord = latitude.toDouble(),
            y_coord = longitude.toDouble(),
            email = email.text.toString()
        )

        sqlHelper = SqlHelper(this)
        sqlHelper!!.addToilet(toilet)
        //If added succesful
        Toast.makeText(baseContext, "Locatie toegevoegd", Toast.LENGTH_LONG).show()

    }

    // Convert address to lon/lat
    fun getLocationFromAddress(strAddress: String?): GeoPoint? {
        val coder = Geocoder(this)
        val address: List<Address>?
        var p1: GeoPoint? = null
        try {
            address = coder.getFromLocationName(strAddress, 5)

            if (address == null) {
                return null
            }
            val location: Address = address[0]
            p1 = GeoPoint(
                (location.latitude) as Double,
                (location.longitude) as Double
            )
            return p1
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

}