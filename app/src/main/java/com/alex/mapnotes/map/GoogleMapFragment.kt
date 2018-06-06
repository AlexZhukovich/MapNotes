package com.alex.mapnotes.map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.support.v4.content.LocalBroadcastManager
import com.alex.mapnotes.DISPLAY_LOCATION
import com.alex.mapnotes.EXTRA_NOTE
import com.alex.mapnotes.data.provider.AddressLocationProvider
import com.alex.mapnotes.ext.checkLocationPermission
import com.alex.mapnotes.model.Note
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapFragment : SupportMapFragment(), MapView, OnMapReadyCallback {
    private var map: GoogleMap? = null

    val presenter by lazy { GoogleMapPresenter() }
    private val locationProvider by lazy { AddressLocationProvider(this.context!!) }

    var isInteractionMode: Boolean = false
        set(value) {
            field = value
            presenter.handleInteractionMode(value)
        }

    private val displayOnMapBroadcastListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val note = intent?.getParcelableExtra<Note>(EXTRA_NOTE)
            presenter.handleMapNote(note)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        locationProvider.startLocationUpdates()
        locationProvider.addUpdatableLocationListener {
            presenter.handleLocationUpdate(isInteractionMode, it)
        }
        getMapAsync(this)
    }

    override fun animateCamera(currentLocation: Location?) {
        map?.animateCamera(CameraUpdateFactory.newLatLng(
                currentLocation?.let { LatLng(it.latitude, it.longitude) }
        ))
    }

    override fun displayMoteOnMap(note: Note) {
        val notePos = LatLng(note.latitude, note.longitude)
        map?.addMarker(MarkerOptions()
                .position(notePos)
                .title(note.text))?.showInfoWindow()
        map?.animateCamera(CameraUpdateFactory.newLatLng(notePos))
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager
                .getInstance(this.context!!)
                .registerReceiver(displayOnMapBroadcastListener, IntentFilter(DISPLAY_LOCATION))
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        updateInitLocation(map)
        if (checkLocationPermission(this.context!!)) {
            map?.isMyLocationEnabled = true
            map?.setOnMyLocationButtonClickListener {
                isInteractionMode = false
                return@setOnMyLocationButtonClickListener true
            }
            map?.setOnCameraMoveStartedListener { reason ->
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    isInteractionMode = true
                }
            }
        }
    }

    private fun updateInitLocation(map: GoogleMap?) {
        val defaultZoom = 18.0f
        val initialLoc = map?.cameraPosition?.target
        val location = CameraUpdateFactory.newLatLngZoom(initialLoc, defaultZoom)
        map?.moveCamera(location)
    }

    fun clearAllMarkers() {
        map?.clear()
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this.context!!).unregisterReceiver(displayOnMapBroadcastListener)
    }

    override fun onStop() {
        locationProvider.stopLocationUpdates()
        presenter.onDetach()
        super.onStop()
    }
}