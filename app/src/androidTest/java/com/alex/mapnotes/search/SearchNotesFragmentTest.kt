package com.alex.mapnotes.search

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.searchNoteFragment
import com.alex.mapnotes.robots.testFragmentActivity
import com.alex.mapnotes.robots.testScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
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

    @Rule @JvmField
    val activityRule = testFragmentActivity

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun shouldVerifyLayout() {
        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
        }
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
            isSuccessfullyDisplayedSearchScreen()
        }
    }

    @Test
    fun shouldDisplayNotes() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingListOfNotes(testNotes)
        }
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
            isSearchResultHasNumberItems(expectedItemCount)
            isSearchResultsHaveNotes(testNotes)
        }
    }

    @Test
    fun shouldDisplayLoadingNotesError() {
        prepare(testScope) {
            mockErrorDuringLoadingNotes()
        }
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
            isSuccessfullyDisplayedSearch()
            isSearchResultHasNumberItems(0)
            isErrorDuringLoadingNotesDisplayed()
        }
    }

    @Test
    fun shouldDisplayUnknownUserUser() {
        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
            mockErrorDuringLoadingUserNames()
        }
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
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
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
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
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
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
        testScreen { attachFragment(SearchNotesFragment()) }
        searchNoteFragment {
            searchNoteByText(searchInput)
            searchNoteByText(emptySearchInput)
            isSearchResultHasNumberItems(expectedItemCount)
            isSearchResultsHaveNotes(testNotes)
        }
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
}