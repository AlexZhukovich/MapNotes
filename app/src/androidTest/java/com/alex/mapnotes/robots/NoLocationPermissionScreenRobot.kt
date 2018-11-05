package com.alex.mapnotes.robots

import com.alex.mapnotes.R

fun noLocationPermissionScreen(func: NoLocationPermissionScreenRobot.() -> Unit) =
        NoLocationPermissionScreenRobot().apply { func() }

class NoLocationPermissionScreenRobot : BaseTestRobot() {

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