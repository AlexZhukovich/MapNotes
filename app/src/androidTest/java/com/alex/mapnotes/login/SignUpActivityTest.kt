package com.alex.mapnotes.login

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.MockMapNotesApp
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.login.signup.SignUpActivity
import com.alex.mapnotes.model.AuthUser
import com.alex.mapnotes.testAppModule
import io.mockk.coEvery
import io.mockk.every
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class SignUpActivityTest {
    private val username = "testUserName"
    private val incorrectEmail = "test"
    private val correctEmail = "test@test.com"
    private val password = "password"

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule @JvmField
    val activityRule = ActivityTestRule<SignUpActivity>(SignUpActivity::class.java, true, false)

    @Before
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, SignUpActivity::class.java))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        onView(withId(R.id.signUp))
                .perform(click())

        onView(withText(R.string.error_email_should_be_valid))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        onView(withId(R.id.email))
                .perform(replaceText(incorrectEmail))

        onView(withId(R.id.signUp))
                .perform(click())

        onView(withText(R.string.error_email_should_be_valid))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.signUp))
                .perform(click())

        onView(withText(R.string.error_password_should_not_be_empty))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayNameErrorWhenNameIsEmpty() {
        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.password))
                .perform(replaceText(password))

        onView(withId(R.id.signUp))
                .perform(click())

        onView(withText(R.string.error_name_should_not_be_empty))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplaySignUpErrorAfterSignUpError() {
        coEvery { MockMapNotesApp.mockedUserRepository.signUp(any(), any()) } returns Result.Error(Exception("SignUp error"))

        onView(withId(R.id.name))
                .perform(replaceText(username))

        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.password))
                .perform(replaceText(password))

        onView(withId(R.id.signUp))
                .perform(click())

        onView(withText("SignUp error"))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenMapScreenAfterSuccessfulSignUp() {
        Intents.init()
        val authUser = AuthUser("111111")
        every { MockMapNotesApp.mockedLocationProvider.startLocationUpdates() } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.stopLocationUpdates() } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.addUpdatableLocationListener(any()) } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.isLocationAvailable() } returns false
        coEvery { MockMapNotesApp.mockedUserRepository.changeUserName(authUser, username) } answers { nothing }
        coEvery { MockMapNotesApp.mockedUserRepository.signUp(correctEmail, password) } returns Result.Success(authUser)

        onView(withId(R.id.name))
                .perform(replaceText(username))

        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.password))
                .perform(replaceText(password))

        onView(withId(R.id.signUp))
                .perform(click())

        Intents.intended(hasComponent(HomeActivity::class.java.name))

        Intents.release()
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}