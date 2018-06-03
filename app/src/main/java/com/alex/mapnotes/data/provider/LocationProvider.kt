package com.alex.mapnotes.data.provider

import android.location.Location

interface LocationProvider {

    fun startLocationUpdates()

    fun addLocationListener(listener: (Location) -> Unit)

    fun stopLocationUpdates()
}