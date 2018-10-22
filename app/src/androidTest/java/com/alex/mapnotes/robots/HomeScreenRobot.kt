package com.alex.mapnotes.robots

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.R
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.idlingresources.RecyclerViewSizeIdlingResources
import com.alex.mapnotes.idlingresources.ViewVisibilityIdlingResource
import com.alex.mapnotes.matchers.RecyclerViewMatchers.atPosition
import com.alex.mapnotes.model.Note

fun homeScreen(func: HomeScreenRobot.() -> Unit) = HomeScreenRobot().apply { func() }

val homeScreenMockActivityRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

class HomeScreenRobot : BaseTestRobot() {
    private val searchUserCategoryPosition = 1

    fun display() {
        homeScreenMockActivityRule.launchActivity(null)
    }

    fun signOut() {
        openActionBarOverflowOrOptionsMenu(getActivityInstance())
        clickButtonWithText(R.string.nav_sign_out_title)
    }

    fun isMapDisplayed() {
        val mapVisibilityIdlingResource =
            ViewVisibilityIdlingResource(R.id.mapContainer, View.VISIBLE)
        IdlingRegistry.getInstance().register(mapVisibilityIdlingResource)

        matchDisplayedView(R.id.mapContainer)

        IdlingRegistry.getInstance().unregister(mapVisibilityIdlingResource)
    }

    fun verifyNoteHint(textId: Int) = matchHint(R.id.note, textId)

    fun openAddNoteFragment() {
        matchDisplayedView(R.id.navigation_add_note)
        clickView(R.id.navigation_add_note)

        matchDisplayedView(R.id.bottomSheetContainer)
    }

    fun enterNoteText(text: String) {
        enterText(R.id.note, text)
    }

    fun pressAddButton() = clickView(R.id.add)

    fun addNote(text: String) {
        enterNoteText(text)
        clickView(R.id.add)
    }

    fun openSearch() {
        clickView(R.id.navigation_search_notes)

        matchNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_search_notes)
    }

    fun searchNoteByText(text: String) {
        enterText(R.id.searchText, text)
        clickView(R.id.searchButton)
    }

    fun searchNoteByUser(text: String) {
        enterText(R.id.searchText, text)
        clickView(R.id.searchOptions)
        changeSpinnerSelectedItemPosition(searchUserCategoryPosition)
        clickView(R.id.searchButton)
    }

    fun isNoteInSearchResult(noteText: String) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        matchRecyclerViewItemWithText(R.id.recyclerView, noteText)
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    fun verifyUnknownUserError() =
            matchDisplayedText(R.string.unknown_user_error)

    fun isNotesInSearchResult(notes: List<Note>) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        for (index in 0 until notes.size - 1) {
            onView(withId(R.id.recyclerView))
                    .check(matches(atPosition(index, hasDescendant(withText(notes[index].text)))))
        }
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    fun verifySearchResultsByItemCount(itemCount: Int) =
            matchRecyclerItemCount(R.id.recyclerView, itemCount)

    fun verifySearchScreen() {
        matchDisplayedView(R.id.recyclerView)
        matchHint(R.id.searchText, R.string.search_hint)
        matchDisplayedView(R.id.searchOptions)
        matchSpinnerText(R.id.searchOptions, R.string.search_notes_category)
        matchText(R.id.searchButton, R.string.search_button_text)
    }

    fun verifyAddButtonIsEnabled() = matchViewIsEnabled(R.id.add)

    fun verifyAddButtonIsDisabled() = matchViewIsDisabled(R.id.add)
    fun verifyNoteText(text: String) = matchText(R.id.note, text)

    fun isSuccessfullyDisplayed() {
        matchNavigationItemCount(R.id.navigation, 3)
        matchNavigationHasItemTitle(R.id.navigation, getActivityInstance().getString(R.string.nav_add_note_title))
        matchNavigationHasItemTitle(R.id.navigation, getActivityInstance().getString(R.string.nav_map_title))
        matchNavigationHasItemTitle(R.id.navigation, getActivityInstance().getString(R.string.nav_search_notes_title))
        matchNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_map)
    }

    fun openAddNote() {
        clickView(R.id.navigation_add_note)
        matchNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_add_note)
    }

    fun openMap() {
        clickView(R.id.navigation_map)
        matchNavigationHasCheckedItemId(R.id.navigation, R.id.navigation_map)
    }

    fun isSuccessfullyDisplayedAddNote() {
        matchHint(R.id.note, R.string.add_note_hint)
        matchViewIsDisabled(R.id.add)
    }

    fun isSuccessfullyDisplayedSearch() {
        matchHint(R.id.searchText, R.string.search_hint)
        matchDisplayedView(R.id.searchButton)
//        matchDisplayedView(R.id.searchOptions)
    }

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }
}