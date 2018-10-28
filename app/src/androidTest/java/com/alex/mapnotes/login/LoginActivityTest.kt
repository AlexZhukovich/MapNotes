package com.alex.mapnotes.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.signInScreen
import com.alex.mapnotes.robots.signUpScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : MockTest() {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun shouldLaunchSignInActivityAfterClickToSignInButton() {
        loginScreen {
            openSignIn()
        }
        signInScreen {
            isSuccessfullyLoaded()
        }
    }

    @Test
    fun shouldLaunchSignUpActivityAfterClickToSignUpButton() {
        loginScreen {
            openSignUp()
        }
        signUpScreen {
            isSuccessfullyLoaded()
        }
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
}