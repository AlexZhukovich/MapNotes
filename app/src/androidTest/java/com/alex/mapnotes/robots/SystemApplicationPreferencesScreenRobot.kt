package com.alex.mapnotes.robots

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.By
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.Until
import org.junit.Assert.assertEquals

fun systemAppPreferenceScreen(func: SystemApplicationPreferencesScreenRobot.() -> Unit) =
        SystemApplicationPreferencesScreenRobot().apply { func() }

class SystemApplicationPreferencesScreenRobot : BaseTestRobot() {

    fun closeAppPreferenceScreen() {
        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.pressBack()
    }

    fun isAppPreferencesDisplayed(expectedAppName: String) {
        val expectedAppNameRes = "com.android.settings:id/entity_header_title"
        val timeout = 5_000L

        val uiDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        uiDevice.wait(Until.hasObject(By.text(expectedAppName)), timeout)
        val appName = uiDevice.findObject(By.res(expectedAppNameRes))
        assertEquals(expectedAppName, appName.text)
    }
}