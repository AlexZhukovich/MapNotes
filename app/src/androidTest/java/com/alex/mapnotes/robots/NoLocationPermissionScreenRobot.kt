package com.alex.mapnotes.robots

import com.alex.mapnotes.R

fun noLocationPermissionScreen(func: NoLocationPermissionScreenRobot.() -> Unit) =
        NoLocationPermissionScreenRobot().apply { func() }

class NoLocationPermissionScreenRobot : BaseTestRobot() {

    fun openApplicationPreferences() {
        clickOnView(R.id.openAppPrefs)
    }
}