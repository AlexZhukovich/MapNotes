package com.alex.mapnotes.nopermissions

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isClickable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import com.alex.mapnotes.R
import com.alex.mapnotes.FragmentTestActivity
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoLocationPermissionFragmentTest {

    @Rule @JvmField
    val activityRule = ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java)

    @Before
    fun setUp() {
        activityRule.activity.setFragment(NoLocationPermissionFragment())
    }

    @Test
    fun shouldVerifyLayoutOfFragment() {
        onView(withId(R.id.mapImage))
                .check(matches(isDisplayed()))

        onView(withId(R.id.permissionExplanation))
                .check(matches(withText(R.string.permission_explanation)))

        onView(withId(R.id.openAppPrefs))
                .check(matches(withText(R.string.open_app_prefs)))
                .check(matches(isEnabled()))
                .check(matches(isClickable()))
    }

    @Test
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