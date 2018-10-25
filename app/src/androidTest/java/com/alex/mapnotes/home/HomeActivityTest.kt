package com.alex.mapnotes.home

import androidx.test.espresso.intent.Intents
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.homeScreenMockActivityRule
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.prepare
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeActivityTest : MockTest() {

    private val activityRule = homeScreenMockActivityRule
    private val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(activityRule)

    @Before
    override fun setUp() {
        super.setUp()
        prepare(testScope) {
            mockLocationProvider(true)
            mockAuthorizedUser()
        }
    }

    @Test
    fun shouldVerifyAllTabs() {
        homeScreen {
            displayAsEntryPoint()
            isSuccessfullyDisplayed()
        }
    }

    @Test
    fun shouldVerifyAddNoteFragment() {
        homeScreen {
            displayAsEntryPoint()
            openAddNote()
            isSuccessfullyDisplayedAddNote()
        }
    }

    @Test @Ignore
    fun shouldVerifySearchNoteFragment() {
        val notes = listOf(Note(text = "test note", user = "test user"))
        prepare(testScope) {
            mockLoadingListOfNotes(notes)
        }
        homeScreen {
            displayAsEntryPoint()
            isSuccessfullyDisplayed()
            openSearch()
            isSuccessfullyDisplayedSearch()
        }
    }

    @Test
    fun shouldVerifyMapFragmentAfterMovingFromAddTab() {
        homeScreen {
            displayAsEntryPoint()
            openAddNote()
            openMap()
        }
    }

    @Test
    fun shouldVerifySignOut() {
        Intents.init()
        prepare(testScope) {
            mockUserSignOut()
        }
        homeScreen {
            displayAsEntryPoint()
            signOut()
        }
        loginScreen {
            isSuccessfullyLoaded()
        }
        Intents.release()
    }
}