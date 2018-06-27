package com.alex.mapnotes.data.formatter

interface LatLonFormatter {

    fun format(lat: Double, lon: Double): String
}