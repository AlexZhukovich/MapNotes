package com.alex.mapnotes.home

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
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.nopermissions.NoLocationPermissionFragment
import com.alex.mapnotes.R
import com.alex.mapnotes.add.AddNoteFragment
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.LOCATION_REQUEST_CODE
import com.alex.mapnotes.ext.checkLocationPermission
import com.alex.mapnotes.ext.navigateTo
import com.alex.mapnotes.ext.requestLocationPermissions
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.map.GoogleMapFragment
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.search.SearchNotesFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.button_sheet.*

const val DISPLAY_LOCATION = "display_location"
const val EXTRA_NOTE = "note"

class HomeActivity : AppCompatActivity(), HomeView {
    private var mapFragment: GoogleMapFragment? = null
    private val bottomSheetBehavior by lazy { BottomSheetBehavior.from(bottomSheet) }
    private val appExecutors: AppExecutors by lazy { AppExecutors() }
    private val userRepository: UserRepository by lazy { FirebaseUserRepository(appExecutors) }
    private val presenter: HomeMvpPresenter by lazy { HomePresenter(appExecutors, userRepository) }

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
        mapFragment?.onStart()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        presenter.checkUser()
    }

    override fun onResume() {
        super.onResume()
        mapFragment?.onResume()

        navigation.selectedItemId = R.id.navigation_map
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomSheetBehavior.setBottomSheetCallback(bottomSheetCallback)

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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
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
        mapFragment?.isInteractionMode = isInteractionMode
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
        mapFragment = GoogleMapFragment()
        supportFragmentManager.beginTransaction()
                .replace(R.id.mapContainer, mapFragment)
                .commit()
        navigation.visibility = View.VISIBLE
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
        if (mapFragment?.isInteractionMode!! || mapFragment?.markers?.isNotEmpty()!!) {
            mapFragment?.isInteractionMode = false
            mapFragment?.clearAllMarkers()
        } else {
            super.onBackPressed()
        }
    }

    override fun onPause() {
        super.onPause()
        mapFragment?.onPause()
        navigation.setOnNavigationItemSelectedListener(null)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(hideExpandedMenuListener)
    }

    override fun onStop() {
        presenter.onDetach()
        mapFragment?.onStop()
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
