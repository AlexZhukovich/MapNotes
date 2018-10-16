package com.alex.mapnotes.robots

import android.view.View
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import com.alex.mapnotes.R
import com.alex.mapnotes.idlingresources.RecyclerViewSizeIdlingResources
import com.alex.mapnotes.idlingresources.ViewVisibilityIdlingResource

fun homeScreen(func: HomeScreenRobot.() -> Unit) = HomeScreenRobot().apply { func() }

class HomeScreenRobot : BaseTestRobot() {

    fun signOut() {
        openActionBarOverflowOrOptionsMenu(getActivityInstance())
        clickButtonWithText(R.string.nav_sign_out_title)
    }

    fun verifyMap() {
        val mapVisibilityIdlingResource =
            ViewVisibilityIdlingResource(R.id.mapContainer, View.VISIBLE)
        IdlingRegistry.getInstance().register(mapVisibilityIdlingResource)

        matchDisplayedView(R.id.mapContainer)

        IdlingRegistry.getInstance().unregister(mapVisibilityIdlingResource)
    }

    fun verifyNoteHint(textId: Int) = matchHint(R.id.note, textId)

    fun openAddNoteFragment() {
        matchDisplayedView(R.id.navigation_add_note)
        clickButton(R.id.navigation_add_note)

        matchDisplayedView(R.id.bottomSheetContainer)
    }

    fun enterNoteText(text: String) {
        enterText(R.id.note, text)
    }

    fun pressAddButton() = clickButton(R.id.add)

    fun addNote(text: String) {
        enterNoteText(text)
        clickButton(R.id.add)
    }

    fun openSearchFragment() {
        clickButton(R.id.navigation_search_notes)

        matchDisplayedView(R.id.searchText)
    }

    fun searchNoteByText(text: String) {
        enterText(R.id.searchText, text)
        clickButton(R.id.searchButton)
    }

    fun verifySearchResults(noteText: String) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        matchRecyclerViewItemWithText(R.id.recyclerView, noteText)
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    fun verifyAddButtonIsEnabled() = matchViewInEnabled(R.id.add)

    fun verifyAddButtonIsDisabled() = matchViewIsDisabled(R.id.add)
    fun verifyNoteText(text: String) = matchText(R.id.note, text)
}