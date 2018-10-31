package com.alex.mapnotes.nopermissions

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.noLocationPermissionScreen
import com.alex.mapnotes.robots.systemAppPreferenceScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoLocationPermissionFragmentTest {

    @Rule
    @JvmField
    val activityRule = ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java)

    @Before
    fun setUp() {
        activityRule.activity.setFragment(NoLocationPermissionFragment())
    }

    @Test
    fun shouldVerifyLayoutOfFragment() {
        noLocationPermissionScreen {
            isSuccessfullyLoaded()
        }
    }

    @Test
    fun shouldVerifyOpeningAppPreferences() {
        val appName = activityRule.activity.getString(R.string.app_name)

        noLocationPermissionScreen {
            openApplicationPreferences()
        }
        systemAppPreferenceScreen {
            isAppPreferencesDisplayed(appName)
            closeAppPreferenceScreen()
        }
    }
}