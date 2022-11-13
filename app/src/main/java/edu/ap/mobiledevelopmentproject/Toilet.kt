package edu.ap.mobiledevelopmentproject

class Toilet {
    var street: String? = null
    var number: String? = null
    var postal_code: Int? = null
    var district: String? = null
    var target_audience: String? = null
    var wheelchair_accessible: String? = null
    var changing_table: String? = null
    var x_coord: Double? = null
    var y_coord: Double? = null

    constructor()

    constructor(
        street: String?,
        number: String?,
        postal_code: Int?,
        district: String?,
        target_audience: String?,
        wheelchair_accessible: String?,
        changing_table: String?,
        x_coord: Double?,
        y_coord: Double?
    ) {
        this.street = street
        this.number = number
        this.postal_code = postal_code
        this.district = district
        this.target_audience = target_audience
        this.wheelchair_accessible = wheelchair_accessible
        this.changing_table = changing_table
        this.x_coord = x_coord
        this.y_coord = y_coord
    }
}