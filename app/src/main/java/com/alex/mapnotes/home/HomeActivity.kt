package com.alex.mapnotes.home

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alex.mapnotes.R
import com.alex.mapnotes.add.AddNoteFragment
import com.alex.mapnotes.ext.LOCATION_REQUEST_CODE
import com.alex.mapnotes.ext.checkLocationPermission
import com.alex.mapnotes.ext.navigateTo
import com.alex.mapnotes.ext.requestLocationPermissions
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.map.MapFragment
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.nopermissions.NoLocationPermissionFragment
import com.alex.mapnotes.search.SearchNotesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*
import org.koin.android.ext.android.inject

const val DISPLAY_LOCATION = "display_location"
const val EXTRA_NOTE = "note"

class HomeActivity : AppCompatActivity(), HomeView {
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheet) }

    private val mapFragment: MapFragment by inject()
    private val presenter: HomeMvpPresenter by inject()

    private val hideExpandedMenuListener = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.getParcelableExtra<Note>(EXTRA_NOTE) != null) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private val bottomSheetCallback: BottomSheetBehavior.BottomSheetCallback
        get() {
            return object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    // not needed
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN &&
                            navigation.selectedItemId != R.id.navigation_map) {
                        navigation.selectedItemId = R.id.navigation_map
                    }
                }
            }
        }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        if (navigation.selectedItemId == item.itemId &&
                bottomSheetBehavior.state != BottomSheetBehavior.STATE_HIDDEN) {
            return@OnNavigationItemSelectedListener false
        }
        return@OnNavigationItemSelectedListener presenter.handleNavigationItemClick(item.itemId)
    }

    private fun replaceBottomFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.bottomSheetContainer, fragment)
                .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        presenter.checkUser()
    }

    override fun onResume() {
        super.onResume()

        navigation.selectedItemId = R.id.navigation_map
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomSheetBehavior.addBottomSheetCallback(bottomSheetCallback)

        if (!checkLocationPermission(this)) {
            // Permission is not granted; show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                presenter.showLocationPermissionRationale()
            } else {
                // No explanation needed, we can request the permission.
                requestLocationPermissions(this)
            }
        } else {
            showContentWhichRequirePermissions()
        }

        LocalBroadcastManager
                .getInstance(this)
                .registerReceiver(hideExpandedMenuListener, IntentFilter(DISPLAY_LOCATION))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_sign_out -> presenter.signOut()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun displayAddNote() {
        replaceBottomFragment(AddNoteFragment())
    }

    override fun displaySearchNotes() {
        replaceBottomFragment(SearchNotesFragment())
    }

    override fun updateMapInteractionMode(isInteractionMode: Boolean) {
        mapFragment.isInteractionMode = isInteractionMode
    }

    override fun updateNavigationState(newState: Int) {
        bottomSheetBehavior.state = newState
    }

    override fun showPermissionExplanationSnackBar() {
        Snackbar.make(layout, R.string.permission_explanation, Snackbar.LENGTH_LONG)
                .setAction(R.string.permission_grant_text) { requestLocationPermissions(this) }
                .show()
    }

    override fun showContentWhichRequirePermissions() {
        mapFragment.let { mapFragment ->
            supportFragmentManager.beginTransaction()
                    .replace(R.id.mapContainer, mapFragment.fragment)
                    .commit()
            navigation.visibility = View.VISIBLE
        }
    }

    override fun hideContentWhichRequirePermissions() {
        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, NoLocationPermissionFragment())
                .commit()
        navigation.visibility = View.GONE
    }

    override fun navigateToLoginScreen() {
        finish()
        navigateTo(LoginActivity::class.java)
    }

    override fun onBackPressed() {
        if (mapFragment.isInteractionMode || mapFragment.hasMarkersOnMap()) {
            mapFragment.isInteractionMode = false
            mapFragment.clearAllMarkers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        bottomSheetBehavior.removeBottomSheetCallback(bottomSheetCallback)
        navigation.setOnNavigationItemSelectedListener(null)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(hideExpandedMenuListener)
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
