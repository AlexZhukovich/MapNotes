package com.alex.mapnotes.login

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.Intents.intended
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.R
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.login.signup.SignUpActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<LoginActivity>(LoginActivity::class.java)

    @Before
    fun setUp() {
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
    }
}