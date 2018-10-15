package com.alex.mapnotes.login

import android.content.Intent
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.model.AuthUser
import com.alex.mapnotes.testAppModule
import io.mockk.coEvery
import io.mockk.every
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject

@RunWith(AndroidJUnit4::class)
class SignInActivityTest : KoinTest {
    private val incorrectEmail = "test"
    private val correctEmail = "test@test.com"
    private val password = "password"

    private val userRepository: UserRepository by inject()
    private val locationProvider: LocationProvider by inject()

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule @JvmField
    val activityRule = ActivityTestRule<SignInActivity>(SignInActivity::class.java, true, false)

    @Before
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, SignInActivity::class.java))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_email_should_be_valid))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        onView(withId(R.id.email))
                .perform(replaceText(incorrectEmail))

        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_email_should_be_valid))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_password_should_not_be_empty))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldDisplaySignInErrorAfterSignInError() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Error(Exception("SignIn error"))

        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.password))
                .perform(replaceText(password))

        onView(withId(R.id.signIn))
                .perform(click())

        onView(withText(R.string.error_user_cannot_be_authenticated))
                .check(matches(isDisplayed()))
    }

    @Test
    fun shouldOpenMapScreenAfterSuccessfulSignIn() {
        Intents.init()
        val authUser = AuthUser("111111")
        every { locationProvider.startLocationUpdates() } answers { nothing }
        every { locationProvider.stopLocationUpdates() } answers { nothing }
        every { locationProvider.addUpdatableLocationListener(any()) } answers { nothing }
        every { locationProvider.isLocationAvailable() } returns false
        coEvery { userRepository.signIn(correctEmail, password) } returns Result.Success(authUser)
        coEvery { userRepository.getCurrentUser() } returns Result.Success(authUser)

        onView(withId(R.id.email))
                .perform(replaceText(correctEmail))

        onView(withId(R.id.password))
                .perform(replaceText(password))

        onView(withId(R.id.signIn))
                .perform(click())

        Intents.intended(hasComponent(HomeActivity::class.java.name))

        Intents.release()
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}