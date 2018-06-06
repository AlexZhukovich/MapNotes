package com.alex.mapnotes

import android.support.test.espresso.Espresso
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.login.signup.SignUpActivity
import com.alex.mapnotes.matcher.RootMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpValidationTest {
    private val incorrectEmail = "test"
    private val correctEmail = "test@test.com"
    private val password = "password"

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<SignUpActivity>(SignUpActivity::class.java)

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.signUp))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.error_email_should_be_valid))
                .inRoot(RootMatcher.isRoot)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        Espresso.onView(ViewMatchers.withId(R.id.email))
                .perform(ViewActions.replaceText(incorrectEmail))

        Espresso.onView(ViewMatchers.withId(R.id.signUp))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.error_email_should_be_valid))
                .inRoot(RootMatcher.isRoot)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.email))
                .perform(ViewActions.replaceText(correctEmail))

        Espresso.onView(ViewMatchers.withId(R.id.signUp))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.error_password_should_not_be_empty))
                .inRoot(RootMatcher.isRoot)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun shouldDisplayNameErrorWhenNameIsEmpty() {
        Espresso.onView(ViewMatchers.withId(R.id.email))
                .perform(ViewActions.replaceText(correctEmail))

        Espresso.onView(ViewMatchers.withId(R.id.password))
                .perform(ViewActions.replaceText(password))

        Espresso.onView(ViewMatchers.withId(R.id.signUp))
                .perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withText(R.string.error_name_should_not_be_empty))
                .inRoot(RootMatcher.isRoot)
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}