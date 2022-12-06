package edu.ap.mobiledevelopmentproject

import android.annotation.SuppressLint
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

    @SuppressLint("ResourceType")
    private fun getFormValue() {
        try {
        var streetname = findViewById<EditText>(R.id.streetname)
        if(streetname.length() == 0) {
            return Message.message(baseContext, "Vul een naam in")
        }

        var housenumber = findViewById<EditText>(R.id.housenumber)
        if(housenumber.length() == 0) {
            return Message.message(baseContext, "Vul een huisnummer in")
        }

        var zipcode = findViewById<EditText>(R.id.zipcode)
        if(zipcode.length() == 0) {
            return Message.message(baseContext, "Vul een postcode in")
        }

        var district = findViewById<EditText>(R.id.district)
        if(district.length() == 0) {
            return Message.message(baseContext, "Vul een stad in")
        }

        var email = findViewById<EditText>(R.id.email)
        if(email.length() == 0) {
            return Message.message(baseContext, "Vul een email in")
        }

        var latitude = ""
        var longitude = ""

        // radio's
        var genderGroup = findViewById<RadioGroup>(R.id.genderGroup);
        var wheelchairGroup = findViewById<RadioGroup>(R.id.wheelchairGroup);
        var damperTableGroup = findViewById<RadioGroup>(R.id.damperTableGroup);

        val selectedGenderId: Int = genderGroup.checkedRadioButtonId
        if(selectedGenderId <= 0) {
            return Message.message(baseContext, "Kies een geslacht optie")
        }

        val selectedWheelchairId: Int = wheelchairGroup.checkedRadioButtonId
        if(selectedWheelchairId <= 0) {
            return Message.message(baseContext, "Kies een rolstoel optie")
        }

        val selectedDamperTableId: Int = damperTableGroup.checkedRadioButtonId
        if(selectedDamperTableId <= 0) {
            return Message.message(baseContext, "Kies een luiertafel optie")
        }

        var gender = findViewById<RadioButton>(selectedGenderId).text;
        var wheelchair = findViewById<RadioButton>(selectedWheelchairId).text;
        var damperTable = findViewById<RadioButton>(selectedDamperTableId).text;


        var address = "${streetname.text} ${housenumber.text}, ${zipcode.text} ${district.text}"
        var location = getLocationFromAddress(address)
        if (location != null) {
            latitude = location.latitude.toString()
            longitude = location.longitude.toString()
        }

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
            Message.message(baseContext, "Locatie toegevoegd!")
            finish()
        } catch (ex: java.lang.Exception) {
            Message.message(baseContext, "Geen geldige locatie!")
            ex.printStackTrace()
        }
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