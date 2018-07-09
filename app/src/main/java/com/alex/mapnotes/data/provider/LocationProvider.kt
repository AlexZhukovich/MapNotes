package com.alex.mapnotes.data.provider

import android.location.Location

interface LocationProvider {

    fun startLocationUpdates()

    fun addUpdatableLocationListener(listener: (Location) -> Unit)

    fun addSingleLocationListener(listener: (Location) -> Unit)

    fun stopLocationUpdates()

    fun isLocationAvailable(): Boolean
}