package com.alex.mapnotes.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.RemoteException
import com.alex.mapnotes.ext.checkLocationPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class AddressLocationProvider(private val context: Context,
                              private val requestInterval: Long = 3_000) : LocationProvider {
    private var updatableListener :((Location) -> Unit)? = null
    private var singleListener: ((Location) -> Unit)? = null
    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(context) }

    private val locationRequest = LocationRequest.create().apply {
        interval = requestInterval
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            updatableListener?.invoke(locationResult.lastLocation)
            if (singleListener != null) {
                singleListener?.invoke(locationResult.lastLocation)
                singleListener = null
            }
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

    override fun addUpdatableLocationListener(listener: (Location) -> Unit) {
        this.updatableListener = listener
    }

    override fun addSingleLocationListener(listener: (Location) -> Unit) {
        this.singleListener = listener
    }

    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    @SuppressLint("MissingPermission")
    private fun useLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                updatableListener?.invoke(it.result)
                if (singleListener != null) {
                    singleListener?.invoke(it.result)
                    singleListener = null
                }
            }
        }
    }

    override fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun isLocationAvailable(): Boolean {
        val locationManager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        var isGpsEnabled = false
        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        } catch (ex: RemoteException) {
            //not needed because GPS disabled
        }
        return isGpsEnabled
    }
}