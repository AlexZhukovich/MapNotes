package com.alex.mapnotes.robots

import com.alex.mapnotes.R

fun noLocationPermissionScreen(func: NoLocationPermissionScreenRobot.() -> Unit) =
        NoLocationPermissionScreenRobot().apply { func() }

class NoLocationPermissionScreenRobot : BaseTestRobot() {

    fun isSuccessfullyLoaded() {
        matchDisplayedView(R.id.mapImage)
        matchText(R.id.permissionExplanation, R.string.permission_explanation)
        matchText(R.id.openAppPrefs, R.string.open_app_prefs)
        matchViewIsEnabled(R.id.openAppPrefs)
    }
}