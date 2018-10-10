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

@RunWith(AndroidJUnit4::class)
class SmokeTests {

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
            .outerRule(activityRule)
            .around(permissionRule)

    @Test
    fun shouldVerifySuccessfulLogin() {
        val email = "test@test.com"
        val password = "test123"

        loginScreen {
            pressSignIn()
        }
        signInScreen {
            enterEmail(email)
            enterPassword(password)
            pressSignIn()
        }
        homeScreen {
            matchMap()
            signOut()
        }
    }

    @Test
    fun shouldVerifyFailureLogin() {
        val email = "test@test.com"
        val password = "test-password"

        loginScreen {
            pressSignIn()
        }
        signInScreen {
            enterEmail(email)
            enterPassword(password)
            pressSignIn()
            matchSignInErrorMessage()
        }
    }
}