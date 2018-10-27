package com.alex.mapnotes

import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.signInScreen
import com.alex.mapnotes.robots.splashScreen
import com.alex.mapnotes.robots.splashActivityE2ETestRule
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class SmokeTests {
    private val correctEmail = "test@test.com"
    private val correctPassword = "test123"
    private val incorrectPassword = "test-password"

    private val permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(splashActivityE2ETestRule)

    @Test
    fun shouldVerifySuccessfulLogin() {
        splashScreen {
            displayAsEntryPoint()
        }
        loginScreen {
            openSignIn()
        }
        signInScreen {
            signIn(correctEmail, correctPassword)
        }
        homeScreen {
            isMapDisplayed()
            signOut()
        }
    }

    @Test
    fun shouldVerifyFailureLogin() {
        splashScreen {
            displayAsEntryPoint()
        }
        loginScreen {
            openSignIn()
        }
        signInScreen {
            signIn(correctEmail, incorrectPassword)
            isSignInErrorDisplayed()
        }
    }

    @Test
    fun shouldVerifyAddingAndSearchNote() {
        val noteText = "test note ${Date().time}"
        splashScreen {
            displayAsEntryPoint()
        }
        loginScreen {
            openSignIn()
        }
        signInScreen {
            signIn(correctEmail, correctPassword)
        }
        homeScreen {
            isMapDisplayed()
            openAddNote()
            addNote(noteText)
            openSearch()
            searchNoteByText(noteText)
            isNoteInSearchHasResults(noteText)
            signOut()
        }
    }
}