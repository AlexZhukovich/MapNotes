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
import com.alex.mapnotes.map.GoogleMapFragment
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.search.SearchNotesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_sheet.*

const val LOCATION_REQUEST_CODE = 100
const val DISPLAY_LOCATION = "display_location"
const val EXTRA_NOTE = "note"

class MainActivity : AppCompatActivity() {
    private var mapFragment: GoogleMapFragment? = null
    private val bottomSheetBehavior by lazy {
        BottomSheetBehavior.from(bottomSheet)
    }

    private val hideExpandedMenuListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getParcelableExtra<Note>(EXTRA_NOTE) != null) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
                mapFragment?.isInteractionMode = true
                replaceBottomFragment(AddNoteFragment())
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                mapFragment?.isInteractionMode = false
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_search_notes -> {
                mapFragment?.isInteractionMode = true
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
        if (mapFragment?.isInteractionMode!!) {
            mapFragment?.isInteractionMode = false
            mapFragment?.clearAllMarkers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        mapFragment?.onResume()
        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(hideExpandedMenuListener, IntentFilter(DISPLAY_LOCATION))
    }

    override fun onPause() {
        super.onPause()
        mapFragment?.onPause()
        navigation.setOnNavigationItemSelectedListener(null)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(hideExpandedMenuListener)
    }

    override fun onStop() {
        super.onStop()
        mapFragment?.onStop()
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
        mapFragment = GoogleMapFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, mapFragment)
                .commit()
        navigation.visibility = View.VISIBLE
    }

    private fun hideContentWhichRequirePermissions() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, NoLocationPermissionFragment())
                .commit()
        navigation.visibility = View.GONE
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
}
