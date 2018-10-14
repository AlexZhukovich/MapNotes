package com.alex.mapnotes.robots

import android.app.Activity
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.alex.mapnotes.matchers.RecyclerViewMatchers

open class BaseTestRobot {

    fun enterText(viewId: Int, text: String): ViewInteraction =
        onView(withId(viewId))
                .perform(replaceText(text), closeSoftKeyboard())

    fun clickButton(viewId: Int): ViewInteraction =
        onView(withId(viewId))
                .perform(click())

    fun clickButtonWithText(textId: Int): ViewInteraction =
            onView(withText(textId))
                    .check(matches(isDisplayed()))
                    .perform(click())

    fun matchDisplayedText(textId: Int): ViewInteraction =
        onView(withText(textId))
                .check(matches(isDisplayed()))

    fun matchDisplayedView(viewId: Int): ViewInteraction =
        onView(withId(viewId))
                .check(matches(isDisplayed()))

    fun matchRecyclerViewItemWithText(viewId: Int, text: String): ViewInteraction =
        onView(withId(viewId))
            .check(matches(RecyclerViewMatchers.withItemText(text)))

    protected fun getActivityInstance(): Activity {
        var currentActivity: Activity? = null
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            if (resumedActivities.iterator().hasNext()) {
                currentActivity = resumedActivities.iterator().next()
            }
        }
        return currentActivity!!
    }
}