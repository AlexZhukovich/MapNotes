package com.alex.mapnotes.login

import androidx.test.espresso.intent.Intents
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.signInScreen
import com.alex.mapnotes.robots.signUpScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
        Intents.init()
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
    fun tearDown() {
        Intents.release()
        closeKoin()
    }
}