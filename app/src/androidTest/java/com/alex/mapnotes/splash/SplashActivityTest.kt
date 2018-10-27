package com.alex.mapnotes.splash

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.splashActivityMockTestRule
import com.alex.mapnotes.robots.splashScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest : MockTest() {

    @Rule @JvmField
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    @Rule @JvmField
    val activityRule = splashActivityMockTestRule

    @Before
    override fun setUp() {
        super.setUp()
        Intents.init()
    }

    @Test
    fun shouldOpenHomeActivityWhenUserIsAuthenticated() {
        prepare(testScope) {
            mockLocationProvider()
            mockAuthorizedUser()
        }
        splashScreen {
            displayMockAsEntryPoint()
        }
        homeScreen {
            isSuccessfullyLoaded()
        }
    }

    @Test
    fun shouldOpenLoginActivityWhenUserIsNotAuthenticated() {
        prepare(testScope) {
            mockNoAuthorizedUser()
        }
        splashScreen {
            displayAsEntryPoint()
        }
        loginScreen {
            isSuccessfullyLoaded()
        }
    }

    @After
    override fun tearDown() {
        Intents.release()
    }
}