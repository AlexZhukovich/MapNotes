package com.alex.mapnotes.home

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.MockMapNotesApp
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.hasItemTitle
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.withItemCount
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.hasCheckedItem
import com.alex.mapnotes.model.AuthUser
import com.alex.mapnotes.testAppModule
import io.mockk.coEvery
import io.mockk.every
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class HomeActivityTest {

    @Rule @JvmField
    val activityRule = ActivityTestRule<HomeActivity>(HomeActivity::class.java, true, false)

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Before
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
        every { MockMapNotesApp.mockedLocationProvider.startLocationUpdates() } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.stopLocationUpdates() } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.addUpdatableLocationListener(any()) } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.isLocationAvailable() } returns true
        coEvery { MockMapNotesApp.mockedUserRepository.getCurrentUser() } returns Result.Success(AuthUser("1111111"))

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

    private fun getStr(resId: Int): String {
        return activityRule.activity.getString(resId)
    }

    @After
    fun tearDown() = closeKoin()
}