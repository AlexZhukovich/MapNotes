package com.alex.mapnotes.nopermissions

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.noLocationPermissionScreen
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Ignore
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

    @Test @Ignore
    fun shouldVerifyOpeningAppPreferences() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        val expectedAppName = "MapNotes"
        val expectedAppNameRes = "com.android.settings:id/entity_header_title"
        val timeout = 3_000L

        onView(withId(R.id.openAppPrefs))
                .perform(click())

        uiDevice.wait(Until.hasObject(By.text(expectedAppName)), timeout)
        val appName = uiDevice.findObject(By.res(expectedAppNameRes))
        assertEquals(expectedAppName, appName.text)
    }
}