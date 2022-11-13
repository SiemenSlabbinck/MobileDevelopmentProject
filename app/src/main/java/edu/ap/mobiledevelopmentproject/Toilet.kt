package edu.ap.mobiledevelopmentproject

import java.io.Serializable

class Toilet : Serializable {
    var street: String? = null
    var number: String? = null
    var postal_code: Int? = null
    var district: String? = null
    var target_audience: String? = null
    var wheelchair_accessible: String? = null
    var changing_table: String? = null
    var x_coord: Double? = null
    var y_coord: Double? = null
}