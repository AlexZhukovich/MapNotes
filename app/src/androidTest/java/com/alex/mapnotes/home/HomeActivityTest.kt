package com.alex.mapnotes.home

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.hasItemTitle
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.withItemCount
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.hasCheckedItem
import com.alex.mapnotes.model.AuthUser
import com.alex.mapnotes.testAppModule
import io.mockk.coEvery
import io.mockk.every
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class HomeActivityTest : KoinTest {

    private val locationProvider: LocationProvider by inject()
    private val userRepository: UserRepository by inject()
    private val notesRepository: NotesRepository by inject()

    @Rule @JvmField
    val activityRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
        every { locationProvider.startLocationUpdates() } answers { nothing }
        every { locationProvider.stopLocationUpdates() } answers { nothing }
        every { locationProvider.addUpdatableLocationListener(any()) } answers { nothing }
        every { locationProvider.isLocationAvailable() } returns true
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser("1111111"))

        activityRule.launchActivity(null)
    }

    @Test
    fun shouldVerifyAllTabs() {
        onView(withId(R.id.navigation))
                .check(matches(withItemCount(3)))
                .check(matches(hasItemTitle(getStr(R.string.nav_add_note_title))))
                .check(matches(hasItemTitle(getStr(R.string.nav_map_title))))
                .check(matches(hasItemTitle(getStr(R.string.nav_search_notes_title))))
    }

    @Test
    fun shouldVerifyMapTabCheckedByDefault() {
        onView(withId(R.id.navigation))
                .check(matches(hasCheckedItem(R.id.navigation_map)))
    }

    @Test
    fun shouldVerifyAddNoteFragment() {
        onView(withId(R.id.navigation_add_note))
                .perform(click())
        onView(withId(R.id.navigation))
                .check(matches(hasCheckedItem(R.id.navigation_add_note)))
        onView(withHint(R.string.add_note_hint))
                .check(matches(isDisplayed()))
        onView(withId(R.id.add))
                .check(matches(not(isEnabled())))
    }

    @Test
    fun shouldVerifySearchNoteFragment() {
        coEvery { notesRepository.getNotes(any()) } returns Result.Success(listOf())

        onView(withId(R.id.navigation_search_notes))
                .perform(click())
        onView(withId(R.id.navigation))
                .check(matches(hasCheckedItem(R.id.navigation_search_notes)))
        onView(withHint(R.string.search_hint))
                .check(matches(isDisplayed()))
        onView(withId(R.id.searchButton))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldVerifyMapFragmentAfterMovingFromAddTab() {
        onView(withId(R.id.navigation_add_note))
                .perform(click())
        onView(withId(R.id.navigation))
                .check(matches(hasCheckedItem(R.id.navigation_add_note)))
        onView(withId(R.id.navigation_map))
                .perform(click())
        onView(withId(R.id.navigation))
                .check(matches(hasCheckedItem(R.id.navigation_map)))
    }

    @Test
    fun shouldVerifySignOut() {
        coEvery { userRepository.signOut() } answers { nothing }

        Intents.init()
        openActionBarOverflowOrOptionsMenu(activityRule.activity)
        onView(withText(R.string.nav_sign_out_title))
                .perform(click())
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
        Intents.release()
    }

    private fun getStr(resId: Int): String {
        return activityRule.activity.getString(resId)
    }

    @After
    fun tearDown() = closeKoin()
}