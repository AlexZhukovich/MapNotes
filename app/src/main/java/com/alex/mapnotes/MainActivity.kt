package com.alex.mapnotes

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.alex.mapnotes.add.AddNoteFragment
import com.alex.mapnotes.data.provider.AddressLocationProvider
import com.alex.mapnotes.ext.checkLocationPermission
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.search.SearchNotesFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_sheet.*

const val LOCATION_REQUEST_CODE = 100
const val DISPLAY_LOCATION = "display_location"
const val EXTRA_NOTE = "note"

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val defaultZoom = 15.0f

    private var isInteractionMode = false

    private var mapFragment: SupportMapFragment? = null

    private val locationProvider by lazy { AddressLocationProvider(this) }

    private var map: GoogleMap? = null
    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheet)
    }

    private val displayOnMapBroadcastListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            isInteractionMode = true
            val note = intent?.getParcelableExtra<Note>(EXTRA_NOTE)
            if (note != null) {
                val notePos = LatLng(note.latitude, note.longitude)

                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                map?.addMarker(MarkerOptions()
                        .position(notePos)
                        .title(note.text))?.showInfoWindow()
                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(notePos, 16.0f))
            }
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (navigation.selectedItemId == item.itemId
                && bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            return@OnNavigationItemSelectedListener false
        }

        when (item.itemId) {
            R.id.navigation_add_note -> {
                isInteractionMode = true
                replaceBottomFragment(AddNoteFragment())
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                isInteractionMode = false
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search_notes -> {
                isInteractionMode = true
                replaceBottomFragment(SearchNotesFragment())
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun replaceBottomFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.bottomSheetContainer, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        navigation.selectedItemId = R.id.navigation_map
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted; show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                showPermissionExplanationSnackBar()
                hideContentWhichRequirePermissions()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_REQUEST_CODE)
            }
        } else {
            showContentWhichRequirePermissions()
        }
    }

    override fun onStart() {
        super.onStart()
        mapFragment?.onStart()
    }

    override fun onBackPressed() {
        if (isInteractionMode) {
            isInteractionMode = false
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        mapFragment?.onResume()
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(displayOnMapBroadcastListener, IntentFilter(DISPLAY_LOCATION))
    }

    override fun onPause() {
        super.onPause()
        mapFragment?.onPause()
        navigation.setOnNavigationItemSelectedListener(null)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(displayOnMapBroadcastListener)
    }

    override fun onStop() {
        super.onStop()
        mapFragment?.onStop()
        locationProvider.stopLocationUpdates()
    }

    private fun showPermissionExplanationSnackBar() {
        Snackbar
                .make(layout, R.string.permission_explanation, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_grant_text) {
                    ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            LOCATION_REQUEST_CODE)
                }
                .show()
    }

    private fun showContentWhichRequirePermissions() {
        mapFragment = SupportMapFragment.newInstance()
        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, mapFragment)
                .commit()
        navigation.visibility = View.VISIBLE
        mapFragment?.getMapAsync(this)
    }

    private fun hideContentWhichRequirePermissions() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, NoLocationPermissionFragment())
                .commit()
        navigation.visibility = View.GONE
        mapFragment?.getMapAsync(null)
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when (requestCode) {
            LOCATION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    showContentWhichRequirePermissions()
                } else {
                    hideContentWhichRequirePermissions()
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        if (checkLocationPermission(this)) {
            updateInitLocation(map)
            this.map?.isMyLocationEnabled = true
            this.map?.setOnMyLocationButtonClickListener {
                isInteractionMode = false
                return@setOnMyLocationButtonClickListener true
            }
            this.map?.setOnCameraMoveStartedListener { reason ->
                if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
                    isInteractionMode = true
                }
            }
            locationProvider.startLocationUpdates()
            locationProvider.addUpdatableLocationListener {
                if (!isInteractionMode) {
                    val zoom = map?.cameraPosition?.zoom!!
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), zoom))
                }
            }
        }
    }

    private fun updateInitLocation(map: GoogleMap?) {
        val initialLoc = map?.cameraPosition?.target
        val location = CameraUpdateFactory.newLatLngZoom(initialLoc, defaultZoom)
        map?.moveCamera(location)
    }
}
