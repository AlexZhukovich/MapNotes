package com.alex.mapnotes.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.alex.mapnotes.ext.checkLocationPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class AddressLocationProvider(private val context: Context) : LocationProvider {
    private var listener :((Location) -> Unit)? = null
    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val locationRequest = LocationRequest.create().apply {
        interval = 3_000
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            listener?.invoke(locationResult.lastLocation)
        }
    }

    @SuppressLint("MissingPermission")
    override fun startLocationUpdates() {
        if (checkLocationPermission(context)) {
            requestLocationUpdates()
        } else {
            useLastLocation()
        }
    }

    override fun addLocationListener(listener: (Location) -> Unit) {
        this.listener = listener
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    @SuppressLint("MissingPermission")
    private fun useLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                listener?.invoke(it.result)
            }
        }
    }

    override fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}