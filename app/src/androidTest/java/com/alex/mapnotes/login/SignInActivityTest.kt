package com.alex.mapnotes.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.mapnotes.InstrumentationTestData
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.signInScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInActivityTest : MockTest() {
    private val signInActivityScope = this

    @get:Rule
    val appPermissionRule = permissionRule

    @Before
    override fun setUp() {
        super.setUp()
        signInScreen {
            launch()
        }
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        signInScreen {
            signIn(InstrumentationTestData.EMPTY_EMAIL, InstrumentationTestData.EMPTY_PASSWORD)
            isIncorrectEmailErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        signInScreen {
            signIn(InstrumentationTestData.INCORRECT_EMAIL, InstrumentationTestData.PASSWORD)
            isIncorrectEmailErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        signInScreen {
            signIn(InstrumentationTestData.CORRECT_EMAIL, InstrumentationTestData.EMPTY_PASSWORD)
            isEmptyPasswordErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplaySignInErrorAfterSignInError() {
        prepare(signInActivityScope) {
            mockUnsuccessfulSignInWithException()
        }
        signInScreen {
            signIn(InstrumentationTestData.CORRECT_EMAIL, InstrumentationTestData.PASSWORD)
            isSignInErrorDisplayed()
        }
    }

    @Test
    fun shouldOpenMapScreenAfterSuccessfulSignIn() {
        prepare(signInActivityScope) {
            mockLocationProvider()
            mockSuccessfulSignIn(InstrumentationTestData.CORRECT_EMAIL, InstrumentationTestData.PASSWORD)
        }
        signInScreen {
            signIn(InstrumentationTestData.CORRECT_EMAIL, InstrumentationTestData.PASSWORD)
        }
        homeScreen {
            isSuccessfullyLoaded()
        }
    }
}