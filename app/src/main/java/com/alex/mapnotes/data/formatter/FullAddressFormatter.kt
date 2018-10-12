package com.alex.mapnotes.data.formatter

import android.location.Geocoder
import com.alex.mapnotes.model.Location

class FullAddressFormatter(private val geocoder: Geocoder) : LocationFormatter {
    private val emptyResult = ""
    private val maxResults = 1
    private val addressLineIndex = 0

    override fun format(location: Location): String {
        val address = geocoder.getFromLocation(location.latitude, location.longitude, maxResults)
        if (address.isNotEmpty()) {
            val firstAddress = address.first()
            return firstAddress.getAddressLine(addressLineIndex)
        }
        return emptyResult
    }
}