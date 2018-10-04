package com.alex.mapnotes.search

import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.MockMapNotesApp
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.matchers.RecyclerViewMatchers.withItemCount
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.testAppModule
import io.mockk.coEvery
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.anything
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Ignore
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class SearchNotesFragmentTest {
    private val searchInput = "text"
    private val emptySearchInput = ""
    private val searchUserCategoryPosition = 1
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
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
    }

    @Test
    fun shouldVerifyLayout() {
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(listOf())
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
    }

    @Test
    fun shouldDisplayNotes() {
        val expectedItemCount = testNotes.size
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(testNotes)
        activityRule.activity.setFragment(SearchNotesFragment())

        onView(withId(R.id.recyclerView))
                .check(matches(withItemCount(expectedItemCount)))
    }

    @Test @Ignore
    fun shouldDisplayLoadingNotesError() {
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Error(RuntimeException())
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
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(listOf())
        coEvery { MockMapNotesApp.mockedUserRepository.getUserIdFromHumanReadableName(any()) } returns Result.Error(RuntimeException())
        activityRule.activity.setFragment(SearchNotesFragment())

        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()))

        onView(withId(R.id.searchText))
                .perform(ViewActions.replaceText(searchInput))

        onView(withId(R.id.searchOptions))
                .perform(click())

        onData(anything())
                .atPosition(searchUserCategoryPosition)
                .inRoot(isPlatformPopup())
                .perform(click())

        onView(withId(R.id.searchButton))
                .perform(click())

        onView(allOf(withId(R.id.snackbar_text), withText(R.string.unknown_user_error)))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldSearchByUserAndDisplayResults() {
        val expectedItemCount = testNotes.size
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(listOf())
        coEvery { MockMapNotesApp.mockedUserRepository.getUserIdFromHumanReadableName(any()) } returns Result.Success(testUID)
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotesByUser(any(), any()) } returns Result.Success(testNotes)
        activityRule.activity.setFragment(SearchNotesFragment())

        onView(withId(R.id.searchText))
                .perform(ViewActions.replaceText(searchInput))

        onView(withId(R.id.searchOptions))
                .perform(click())

        onData(anything())
                .atPosition(searchUserCategoryPosition)
                .inRoot(isPlatformPopup())
                .perform(click())

        onView(withId(R.id.searchButton))
                .perform(click())

        onView(withId(R.id.recyclerView))
                .check(matches(withItemCount(expectedItemCount)))
    }

    @Test
    fun shouldSearchByNotesAndDisplayResult() {
        val expectedItemCount = testNotes.size
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(listOf())
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotesByNoteText(any(), any()) } returns Result.Success(testNotes)
        activityRule.activity.setFragment(SearchNotesFragment())

        onView(withId(R.id.searchText))
                .perform(ViewActions.replaceText(searchInput))

        onView(withId(R.id.searchButton))
                .perform(click())

        onView(withId(R.id.recyclerView))
                .check(matches(withItemCount(expectedItemCount)))
    }

    @Test
    fun shouldSearchIncorrectDataAndDisplayCorrectDataAfterClearRequest() {
        val expectedItemCount = testNotes.size
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(listOf())
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotesByNoteText(any(), any()) } returns Result.Error(RuntimeException())
        coEvery { MockMapNotesApp.mockedNotesRepository.getNotes(any()) } returns Result.Success(testNotes)
        activityRule.activity.setFragment(SearchNotesFragment())

        onView(withId(R.id.searchText))
                .perform(ViewActions.replaceText(searchInput))

        onView(withId(R.id.searchButton))
                .perform(click())

        onView(withId(R.id.searchText))
                .perform(ViewActions.replaceText(emptySearchInput))

        onView(withId(R.id.searchButton))
                .perform(click())

        onView(withId(R.id.recyclerView))
                .check(matches(withItemCount(expectedItemCount)))
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}