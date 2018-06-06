package com.alex.mapnotes

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.matcher.RootMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignInValidationTest {
    private val incorrectEmail = "test"
    private val correctEmail = "test@test.com"

    @Rule @JvmField
    val activityRule = ActivityTestRule<SignInActivity>(SignInActivity::class.java)

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_email_should_be_valid))
                .inRoot(RootMatcher.isRoot)
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        onView(withId(R.id.email))
                .perform(replaceText(incorrectEmail))

        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_email_should_be_valid))
                .inRoot(RootMatcher.isRoot)
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_password_should_not_be_empty))
                .inRoot(RootMatcher.isRoot)
                .check(matches(isDisplayed()))
    }
}