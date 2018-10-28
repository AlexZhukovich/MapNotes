package com.alex.mapnotes.login

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.signInScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest : MockTest() {
    private val emptyEmail = ""
    private val correctEmail = "test@test.com"
    private val incorrectEmail = "test"
    private val password = "password"
    private val emptyPassword = ""
    private val signInActivityScope = this

    private val activityRule = ActivityTestRule<SignInActivity>(SignInActivity::class.java, true, false)

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(activityRule)

    @Before
    override fun setUp() {
        super.setUp()
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, SignInActivity::class.java))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        signInScreen {
            signIn(emptyEmail, emptyPassword)
            isIncorrectEmailErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        signInScreen {
            signIn(incorrectEmail, password)
            isIncorrectEmailErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        signInScreen {
            signIn(correctEmail, emptyPassword)
            isEmptyPasswordErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplaySignInErrorAfterSignInError() {
        prepare(signInActivityScope) {
            mockUnsuccessfulSignInWithException()
        }
        signInScreen {
            signIn(correctEmail, password)
            isSignInErrorDisplayed()
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
    }
}