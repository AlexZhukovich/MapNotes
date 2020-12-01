package com.alex.mapnotes.search

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.searchNoteFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SearchNotesFragmentTest : MockTest() {
    private val searchInput = "text"
    private val emptySearchInput = ""
    private val testUID = "1111111"

    private val testNotes: List<Note> = listOf(
            Note(text = "test note 1_1", user = "11111111"),
            Note(text = "test note 2_1", user = "11111111"),
            Note(text = "test note 1_2", user = "22222222"),
            Note(text = "test note 2_2", user = "22222222"),
            Note(text = "test note 3_2", user = "22222222"))

    @Test
    fun shouldVerifyLayout() {
        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
        }
        searchNoteFragment {
            launch()
            isSuccessfullyDisplayedSearchScreen()
        }
    }

    @Test
    fun shouldDisplayNotes() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingListOfNotes(testNotes)
        }
        searchNoteFragment {
            launch()
            isSearchResultHasNumberItems(expectedItemCount)
            isSearchResultsHaveNotes(testNotes)
        }
    }

    @Test
    fun shouldDisplayUnknownUserUser() {
        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
            mockErrorDuringLoadingUserNames()
        }
        searchNoteFragment {
            launch()
            searchNoteByUser(searchInput)
            isUnknownUserErrorDisplayed()
        }
    }

    @Test
    fun shouldSearchByUserAndDisplayResults() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
            mockSearchUserId(testUID)
            mockSearchNoteByAnyUser(testNotes)
        }
        searchNoteFragment {
            launch()
            searchNoteByUser(searchInput)
            isSearchResultHasNumberItems(expectedItemCount)
            isSearchResultsHaveNotes(testNotes)
        }
    }

    @Test
    fun shouldSearchByNotesAndDisplayResult() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
            mockSearchNoteByAnyText(testNotes)
        }
        searchNoteFragment {
            launch()
            searchNoteByText(searchInput)
            isSearchResultHasNumberItems(expectedItemCount)
            isSearchResultsHaveNotes(testNotes)
        }
    }

    @Test
    fun shouldSearchIncorrectDataAndDisplayCorrectDataAfterClearRequest() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingListOfNotes(testNotes)
            mockLoadingEmptyListOfNotesByNoteText()
            mockLoadingListOfNotes(testNotes)
        }
        searchNoteFragment {
            launch()
            searchNoteByText(searchInput)
            searchNoteByText(emptySearchInput)
            isSearchResultHasNumberItems(expectedItemCount)
            isSearchResultsHaveNotes(testNotes)
        }
    }
}