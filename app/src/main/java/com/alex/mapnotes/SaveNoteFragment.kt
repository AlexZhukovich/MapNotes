package com.alex.mapnotes

import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.mapnotes.ext.checkLocationPermission
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import kotlinx.android.synthetic.main.fragment_add_note.currentLocation
import java.util.Locale

class SaveNoteFragment: Fragment() {
    private val maxAddressNumber = 1

    private val geocoder by lazy { Geocoder(this.context, Locale.getDefault()) }
    private val fusedLocationProviderClient by lazy { LocationServices.getFusedLocationProviderClient(this.context!!) }

    private val locationRequest = LocationRequest.create()
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            updateCurrentLocationText(locationResult.lastLocation)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkLocationPermission(this.context!!)) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                updateCurrentLocationText(it)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (checkLocationPermission(this.context!!)) {
            startLocationUpdates()
        } else {
            useLastLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun useLastLocation() {
        fusedLocationProviderClient.lastLocation.addOnCompleteListener {
            if (it.isSuccessful && it.result != null) {
                updateCurrentLocationText(it.result)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun updateCurrentLocationText(location: Location) {
        val address = geocoder.getFromLocation(location.latitude, location.longitude, maxAddressNumber).first()
        currentLocation.text = address.getAddressLine(0)
    }

    override fun onStop() {
        super.onStop()
        stopLocationUpdates()
    }

    private fun stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }
}