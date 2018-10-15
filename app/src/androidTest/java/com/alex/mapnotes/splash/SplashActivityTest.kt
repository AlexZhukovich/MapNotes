package com.alex.mapnotes.splash

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.UserRepository
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
import org.junit.Ignore
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class SplashActivityTest : KoinTest {

    private val userRepository: UserRepository by inject()
    private val locationProvider: LocationProvider by inject()

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule @JvmField
    val activityRule = ActivityTestRule<SplashActivity>(SplashActivity::class.java, true, false)

    @Before
    fun setUp() {
        loadKoinModules(listOf(testAppModule))
        Intents.init()
    }

    @Test @Ignore
    fun shouldOpenHomeActivityWhenUserIsAuthenticated() {
        every { locationProvider.startLocationUpdates() } answers { nothing }
        every { locationProvider.stopLocationUpdates() } answers { nothing }
        every { locationProvider.addUpdatableLocationListener(any()) } answers { nothing }
        every { locationProvider.isLocationAvailable() } returns false
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser("11111"))
        launchActivity()

        Intents.intended(IntentMatchers.hasComponent(HomeActivity::class.java.name))
    }

    @Test
    fun shouldOpenLoginActivityWhenUserIsNotAuthenticated() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException())
        launchActivity()

        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }

    @After
    fun tearDown() {
        Intents.release()
        closeKoin()
    }

    private fun launchActivity() {
        activityRule.launchActivity(null)
    }
}