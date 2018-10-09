package com.alex.mapnotes.robots

import android.view.View
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import com.alex.mapnotes.R
import com.alex.mapnotes.matchers.ViewVisibilityIdlingResource

fun homeScreen(func: HomeScreenRobot.() -> Unit) = HomeScreenRobot().apply { func() }

class HomeScreenRobot : BaseTestRobot() {

    fun signOut() {
        openActionBarOverflowOrOptionsMenu(getActivityInstance())
    }

    fun matchMap() {
        val mapVisibilityIdlingResource =
            ViewVisibilityIdlingResource(R.id.mapContainer, View.VISIBLE)
        IdlingRegistry.getInstance().register(mapVisibilityIdlingResource)

        matchDisplayedView(R.id.mapContainer)

        IdlingRegistry.getInstance().unregister(mapVisibilityIdlingResource)
    }
}