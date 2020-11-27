package com.alex.mapnotes.splash

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.splashScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SplashActivityTest : MockTest() {

    @Test
    fun shouldOpenHomeActivityWhenUserIsAuthenticated() {
        prepare(testScope) {
            mockLocationProvider()
            mockAuthorizedUser()
        }
        splashScreen {
            launch()
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
            launch()
        }
        loginScreen {
            isSuccessfullyLoaded()
        }
    }
}