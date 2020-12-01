package com.alex.mapnotes.robots

import androidx.fragment.app.testing.launchFragmentInContainer
import com.alex.mapnotes.R
import com.alex.mapnotes.nopermissions.NoLocationPermissionFragment

fun noLocationPermissionScreen(func: NoLocationPermissionScreenRobot.() -> Unit) =
        NoLocationPermissionScreenRobot().apply { func() }

class NoLocationPermissionScreenRobot : BaseTestRobot() {

    fun launch() {
        launchFragmentInContainer<NoLocationPermissionFragment>(
            themeResId = R.style.AppTheme
        )
    }

    fun openApplicationPreferences() {
        clickOnView(R.id.openAppPrefs)
    }

    fun isSuccessfullyLoaded() {
        isViewDisplayed(R.id.mapImage)
        isViewWithTextDisplayed(R.id.permissionExplanation, R.string.permission_explanation)
        isViewWithTextDisplayed(R.id.openAppPrefs, R.string.open_app_prefs)
        isViewEnabled(R.id.openAppPrefs)
    }
}