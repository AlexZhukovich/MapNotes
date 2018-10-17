package com.alex.mapnotes.login

import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.signInScreen
import com.alex.mapnotes.testAppModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class SignInActivityTest : MockTest() {
    private val emptyEmail = ""
    private val correctEmail = "test@test.com"
    private val incorrectEmail = "test"
    private val password = "password"
    private val emptyPassword = ""
    private val signInActivityScope = this

    private val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private val activityRule = ActivityTestRule<SignInActivity>(SignInActivity::class.java, true, false)

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(activityRule)

    @Before
    override fun setUp() {
        super.setUp()
        loadKoinModules(listOf(testAppModule))
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, SignInActivity::class.java))
        Intents.init()
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        signInScreen {
            signIn(emptyEmail, emptyPassword)
            verifyIncorrectEmailErrorMessage()
        }
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        signInScreen {
            signIn(incorrectEmail, password)
            verifyIncorrectEmailErrorMessage()
        }
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        signInScreen {
            signIn(correctEmail, emptyPassword)
            verifyEmptyPasswordErrorMessage()
        }
    }

    @Test
    fun shouldDisplaySignInErrorAfterSignInError() {
        prepare(signInActivityScope) {
            mockUnsuccessfulSignInWithException()
        }
        signInScreen {
            signIn(correctEmail, password)
            verifySignInErrorMessage()
        }
    }

    @Test
    fun shouldOpenMapScreenAfterSuccessfulSignIn() {
        prepare(signInActivityScope) {
            mockLocationProvider()
            mockSuccessfulSignIn(correctEmail, password)
        }
        signInScreen {
            signIn(correctEmail, password)
        }
        homeScreen {
            isSuccessfullyLoaded()
        }
    }

    @After
    override fun tearDown() {
        super.tearDown()
        Intents.release()
        closeKoin()
    }
}