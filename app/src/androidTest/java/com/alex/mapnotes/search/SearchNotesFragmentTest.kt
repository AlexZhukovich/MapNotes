package com.alex.mapnotes.search

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.prepare
import io.mockk.coEvery
import org.hamcrest.Matchers.allOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
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
    val activityRule = ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun shouldVerifyLayout() {
        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
        }
        attachToTestActivity()
        homeScreen {
            verifySearchScreen()
        }
    }

    @Test
    fun shouldDisplayNotes() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingListOfNotes(testNotes)
        }
        attachToTestActivity()
        homeScreen {
            verifySearchResultsByItemCount(expectedItemCount)
            verifySearchResults(testNotes)
        }
    }

    @Test @Ignore
    fun shouldDisplayLoadingNotesError() {
        coEvery { notesRepository.getNotes(any()) } returns Result.Error(RuntimeException())
        activityRule.activity.setFragment(SearchNotesFragment())

        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()))

        onView(withId(R.id.searchText))
                .check(matches(withHint(R.string.search_hint)))

        onView(withId(R.id.searchOptions))
                .check(matches(isDisplayed()))

        onView(withId(R.id.searchOptions))
                .check(matches(withSpinnerText(R.string.search_notes_category)))

        onView(withId(R.id.searchButton))
                .check(matches(withText(R.string.search_button_text)))

        onView(allOf(withId(R.id.snackbar_text), withText(R.string.loading_notes_error)))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayUnknownUserUser() {
        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
            mockErrorDuringLoadingUserNames()
        }
        attachToTestActivity()
        homeScreen {
            searchNoteByUser(searchInput)
            verifyUnknownUserError()
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
        attachToTestActivity()
        homeScreen {
            searchNoteByUser(searchInput)
            verifySearchResultsByItemCount(expectedItemCount)
            verifySearchResults(testNotes)
        }
    }

    @Test
    fun shouldSearchByNotesAndDisplayResult() {
        val expectedItemCount = testNotes.size

        prepare(testScope) {
            mockLoadingEmptyListOfNotes()
            mockSearchNoteByAnyText(testNotes)
        }
        attachToTestActivity()
        homeScreen {
            searchNoteByText(searchInput)
            verifySearchResultsByItemCount(expectedItemCount)
            verifySearchResults(testNotes)
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
        attachToTestActivity()
        homeScreen {
            searchNoteByText(searchInput)
            searchNoteByText(emptySearchInput)
            verifySearchResultsByItemCount(expectedItemCount)
            verifySearchResults(testNotes)
        }
    }

    private fun attachToTestActivity() {
        activityRule.activity.setFragment(SearchNotesFragment())
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
}