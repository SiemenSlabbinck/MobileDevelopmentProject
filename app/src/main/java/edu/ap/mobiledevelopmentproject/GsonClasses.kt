package edu.ap.mobiledevelopmentproject

class Feature {
    var geometry: Geometry? = null
    var properties: Properties? = null
}

class Geometry {
    var coordinates: ArrayList<Double>? = null
}

class Properties {
    var OMSCHRIJVING: String? = null

    var TYPE: String? = null

    var BETALEND: String? = null

    var STRAAT: String? = null

    var HUISNUMMER: String? = null

    var POSTCODE = 0

    var DISTRICT: String? = null

    var DOELGROEP: String? = null

    var INTEGRAAL_TOEGANKELIJK: String? = null

    var LUIERTAFEL: String? = null

    var X_COORD = 0.0

    var Y_COORD = 0.0
}

class ToiletGson {
    var features: ArrayList<Feature>? = null
}