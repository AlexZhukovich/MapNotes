package com.alex.mapnotes.robots

import android.view.View
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import com.alex.mapnotes.R
import com.alex.mapnotes.matchers.NonEmptyTextIdlingResource
import com.alex.mapnotes.matchers.RecyclerViewSizeIdlingResources
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

    fun openAddNoteFragment() = clickButton(R.id.navigation_add_note)

    fun matchAddNoteFragment() =
            matchDisplayedView(R.id.bottomSheetContainer)

    fun enterNote(text: String) = enterText(R.id.note, text)

    fun matchDetectedLocation() {
        val nonEmptyIdlingResource = NonEmptyTextIdlingResource(R.id.currentLocation)
        IdlingRegistry.getInstance().register(nonEmptyIdlingResource)
        matchDisplayedView(R.id.currentLocation)
        IdlingRegistry.getInstance().unregister(nonEmptyIdlingResource)
    }

    fun addNote() = clickButton(R.id.add)

    fun openSearchFragment() = clickButton(R.id.navigation_search_notes)

    fun matchSearchFragment() = matchDisplayedView(R.id.searchText)

    fun searchNoteByText(text: String) {
        enterText(R.id.searchText, text)
        clickButton(R.id.searchButton)
    }

    fun matchSearchResults(noteText: String) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        matchRecyclerViewItemWithText(R.id.recyclerView, noteText)
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }
}