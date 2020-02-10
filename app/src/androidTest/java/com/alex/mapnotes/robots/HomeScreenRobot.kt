package com.alex.mapnotes.robots

import android.view.View
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.intent.KIntent
import com.alex.mapnotes.R
import com.alex.mapnotes.getActivityInstance
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.idlingresources.ViewVisibilityIdlingResource

fun homeScreen(func: HomeScreenRobot.() -> Unit) = HomeScreenRobot().apply { func() }

val homeScreenMockActivityRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

class HomeScreenRobot : BaseTestRobot() {

    fun addNoteFragment(func: HomeAddNoteRobot.() -> Unit) = HomeAddNoteRobot().apply { func() }

    fun searchNoteFragment(func: HomeSearchNoteRobot.() -> Unit) = HomeSearchNoteRobot().apply { func() }

    fun displayAsEntryPoint() {
        homeScreenMockActivityRule.launchActivity(null)
    }

    fun openAddNote() {
        clickOnView(R.id.navigation_add_note)
        isBottomNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_add_note)
    }

    fun openMap() {
        clickOnView(R.id.navigation_map)
        isBottomNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_map)
    }

    fun openSearch() {
        clickOnView(R.id.navigation_search_notes)
        isBottomNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_search_notes)
    }

    fun signOut() {
        openActionBarOverflowOrOptionsMenu(getActivityInstance())
        clickOnViewWithText(R.string.nav_sign_out_title)
    }

    fun isMapDisplayed() {
        val mapVisibilityIdlingResource =
            ViewVisibilityIdlingResource(R.id.mapContainer, View.VISIBLE)
        IdlingRegistry.getInstance().register(mapVisibilityIdlingResource)

        isViewDisplayed(R.id.mapContainer)

        IdlingRegistry.getInstance().unregister(mapVisibilityIdlingResource)
    }

    fun isSuccessfullyDisplayed() {
        isBottomNavigationItemCount(R.id.navigation, 3)
        isBottomNavigationHasItemTitle(R.id.navigation,
                getActivityInstance().getString(R.string.nav_add_note_title))
        isBottomNavigationHasItemTitle(R.id.navigation,
                getActivityInstance().getString(R.string.nav_map_title))
        isBottomNavigationHasItemTitle(R.id.navigation,
                getActivityInstance().getString(R.string.nav_search_notes_title))
        isBottomNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_map)
    }

    fun isSuccessfullyLoaded() {
        KIntent {
            hasComponent(HomeActivity::class.java.name)
        }
    }
}