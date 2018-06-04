package com.alex.mapnotes.data.formatter

class CoordinateFormatter : LatLonFormatter {
    override fun format(lat: Double, lon: Double): String {
        return String.format("(%.04f; %.04f)", lat, lon)
    }
}