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
import com.alex.mapnotes.getActivityInstance
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.idlingresources.RecyclerViewSizeIdlingResources
import com.alex.mapnotes.idlingresources.ViewVisibilityIdlingResource
import com.alex.mapnotes.matchers.RecyclerViewMatchers.atPosition
import com.alex.mapnotes.model.Note

fun homeScreen(func: HomeScreenRobot.() -> Unit) = HomeScreenRobot().apply { func() }

val homeScreenMockActivityRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

class HomeScreenRobot : BaseTestRobot() {
    private val searchUserCategoryPosition = 1

    fun addNoteFragment(func: HomeAddNoteRobot.() -> Unit) = HomeAddNoteRobot().apply { func() }

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

    fun searchNoteByText(text: String) {
        enterText(R.id.searchText, text)
        clickOnView(R.id.searchButton)
    }

    fun searchNoteByUser(text: String) {
        enterText(R.id.searchText, text)
        clickOnView(R.id.searchOptions)
        changeSelectedSpinnerItemPosition(searchUserCategoryPosition)
        clickOnView(R.id.searchButton)
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

    // TODO: change name of the method
    fun isNoteInSearchHasResults(noteText: String) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        isRecyclerViewHasItemWithText(R.id.recyclerView, noteText)
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    // TODO: change name of the method
    fun isNotesInSearchResult(notes: List<Note>) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        for (index in 0 until notes.size - 1) {
            onView(withId(R.id.recyclerView))
                    .check(matches(atPosition(index, hasDescendant(withText(notes[index].text)))))
        }
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    fun isUnknownUserErrorDisplayed() =
            isTextDisplayed(R.string.unknown_user_error)

    fun isSearchResultHasNumberItems(itemCount: Int) =
            isRecyclerViewItemCount(R.id.recyclerView, itemCount)

    fun isSuccessfullyDisplayedSearchScreen() {
        isViewDisplayed(R.id.recyclerView)
        isViewHintDisplayed(R.id.searchText, R.string.search_hint)
        isViewDisplayed(R.id.searchOptions)
        isSpinnerHasText(R.id.searchOptions, R.string.search_notes_category)
        isViewWithTextDisplayed(R.id.searchButton, R.string.search_button_text)
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

    fun isSuccessfullyDisplayedSearch() {
        isViewHintDisplayed(R.id.searchText, R.string.search_hint)
        isViewDisplayed(R.id.searchButton)
    }

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }
}