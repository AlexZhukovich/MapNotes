package com.alex.mapnotes

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.signInScreen
import com.alex.mapnotes.robots.splashScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class SmokeTests {
    private val correctEmail = "test@test.com"
    private val correctPassword = "test123"
    private val incorrectPassword = "test-password"

    @get:Rule
    val appPermissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Test
    fun shouldVerifySuccessfulLogin() {
        splashScreen {
            launch()
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
            launch()
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
            launch()
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
            addNoteFragment {
                addNote(noteText)
            }
            openSearch()
            searchNoteFragment {
                searchNoteByText(noteText)
                isSearchResultsHaveNoteWithTitle(noteText)
            }
            signOut()
        }
    }
}