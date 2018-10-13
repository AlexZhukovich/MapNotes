package com.alex.mapnotes

import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.signInScreen
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext
import java.util.Date

@RunWith(AndroidJUnit4::class)
class SmokeTests {
    private val correctEmail = "test@test.com"
    private val correctPassword = "test123"
    private val incorrectPassword = "test-password"

    private val permissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private val activityRule = object : ActivityTestRule<LoginActivity>(LoginActivity::class.java) {
        override fun beforeActivityLaunched() {
            StandAloneContext.loadKoinModules(listOf(appModule))
            super.beforeActivityLaunched()
        }
    }

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(activityRule)

    @Test
    fun shouldVerifySuccessfulLogin() {
        loginScreen {
            pressSignIn()
        }
        signInScreen {
            signIn(correctEmail, correctPassword)
        }
        homeScreen {
            verifyMap()
            signOut()
        }
    }

    @Test
    fun shouldVerifyFailureLogin() {
        loginScreen {
            pressSignIn()
        }
        signInScreen {
            signIn(correctEmail, incorrectPassword)
            matchSignInErrorMessage()
        }
    }

    @Test
    fun shouldVerifyAddingAndSearchNote() {
        val noteText = "test note ${Date().time}"

        loginScreen {
            pressSignIn()
        }
        signInScreen {
            signIn(correctEmail, correctPassword)
        }
        homeScreen {
            verifyMap()
            openAddNoteFragment()
            addNote(noteText)
            openSearchFragment()
            searchNoteByText(noteText)
            verifySearchResults(noteText)
            signOut()
        }
    }
}