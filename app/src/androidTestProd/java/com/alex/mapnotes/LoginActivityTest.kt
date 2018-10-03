package com.alex.mapnotes

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.login.signup.SignUpActivity
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
        onView(withId(R.id.signIn))
                .perform(click())

        intended(hasComponent(SignInActivity::class.java.name))
    }

    @Test
    fun shouldLaunchSignUpActivityAfterClickToSignUpButton() {
        onView(withId(R.id.signUp))
                .perform(click())

        intended(hasComponent(SignUpActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
        closeKoin()
    }
}