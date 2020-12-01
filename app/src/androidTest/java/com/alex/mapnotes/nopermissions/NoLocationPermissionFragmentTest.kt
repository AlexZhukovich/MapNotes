package com.alex.mapnotes.nopermissions

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.noLocationPermissionScreen
import com.alex.mapnotes.robots.systemAppPreferenceScreen
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoLocationPermissionFragmentTest {

    @Test
    fun shouldVerifyLayoutOfFragment() {
        noLocationPermissionScreen {
            launch()
            isSuccessfullyLoaded()
        }
    }

    @Test @Ignore
    fun shouldVerifyOpeningAppPreferences() {
        val appName = InstrumentationRegistry.getInstrumentation().targetContext.getString(R.string.app_name)

        noLocationPermissionScreen {
            launch()
            openApplicationPreferences()
        }
        systemAppPreferenceScreen {
            isAppPreferencesDisplayed(appName)
            closeAppPreferenceScreen()
        }
    }
}