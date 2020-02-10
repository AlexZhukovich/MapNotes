package com.alex.mapnotes.home

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.homeScreenMockActivityRule
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.prepare
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class HomeActivityTest : MockTest() {

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(homeScreenMockActivityRule)

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
            addNoteFragment {
                isSuccessfullyDisplayedAddNote()
            }
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
            searchNoteFragment {
                isSuccessfullyDisplayedSearch()
            }
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
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
}