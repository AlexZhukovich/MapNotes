package com.alex.mapnotes.splash

import android.content.Intent
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.intent.Intents
import android.support.test.espresso.intent.matcher.IntentMatchers
import android.support.test.rule.ActivityTestRule
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.MockMapNotesApp
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.login.LoginActivity
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
class SplashActivityTest {

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule @JvmField
    val activityRule = ActivityTestRule<SplashActivity>(SplashActivity::class.java, true, false)

    @Before
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
        Intents.init()
    }

    @Test
    fun shouldOpenHomeActivityWhenUserIsAuthenticated() {
        every { MockMapNotesApp.mockedLocationProvider.startLocationUpdates() } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.stopLocationUpdates() } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.addUpdatableLocationListener(any()) } answers { nothing }
        every { MockMapNotesApp.mockedLocationProvider.isLocationAvailable() } returns false
        coEvery { MockMapNotesApp.mockedUserRepository.getCurrentUser() } returns Result.Success(AuthUser("11111"))
        launchActivity()

        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }

    @Test
    fun shouldOpenLoginActivityWhenUserIsNotAuthenticated() {
        coEvery { MockMapNotesApp.mockedUserRepository.getCurrentUser() } returns Result.Error(RuntimeException())
        launchActivity()

        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
        closeKoin()
    }

    private fun launchActivity() {
        activityRule.launchActivity(Intent(InstrumentationRegistry.getInstrumentation().targetContext, SplashActivity::class.java))
    }
}