package com.alex.mapnotes.robots

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import com.alex.mapnotes.R
import com.alex.mapnotes.idlingresources.RecyclerViewSizeIdlingResources
import com.alex.mapnotes.matchers.RecyclerViewMatchers
import com.alex.mapnotes.model.Note

fun searchNoteFragment(func: HomeSearchNoteRobot.() -> Unit) = HomeSearchNoteRobot().apply { func() }

class HomeSearchNoteRobot : BaseTestRobot() {

    private val searchUserCategoryPosition = 1

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

    fun isSearchResultsHaveNoteWithTitle(noteText: String) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        isRecyclerViewHasItemWithText(R.id.recyclerView, noteText)
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    fun isSearchResultsHaveNotes(notes: List<Note>) {
        val recyclerViewIdlingResource = RecyclerViewSizeIdlingResources(R.id.recyclerView)
        IdlingRegistry.getInstance().register(recyclerViewIdlingResource)
        for (index in 0 until notes.size - 1) {
            Espresso.onView(ViewMatchers.withId(R.id.recyclerView))
                    .check(ViewAssertions.matches(RecyclerViewMatchers.atPosition(index, ViewMatchers.hasDescendant(ViewMatchers.withText(notes[index].text)))))
        }
        IdlingRegistry.getInstance().unregister(recyclerViewIdlingResource)
    }

    fun isErrorDuringLoadingNotesDisplayed() =
        isTextDisplayed(R.string.loading_notes_error)

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

    fun isSuccessfullyDisplayedSearch() {
        isViewHintDisplayed(R.id.searchText, R.string.search_hint)
        isViewDisplayed(R.id.searchButton)
    }
}