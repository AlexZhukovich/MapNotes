package com.alex.mapnotes.robots

import android.app.Activity
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.withHint
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import com.alex.mapnotes.matchers.RecyclerViewMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers

open class BaseTestRobot {

    fun enterText(viewId: Int, text: String): ViewInteraction =
        onView(withId(viewId))
                .perform(replaceText(text), closeSoftKeyboard())

    fun clickView(viewId: Int): ViewInteraction =
        onView(withId(viewId))
                .perform(click())

    fun clickButtonWithText(textId: Int): ViewInteraction =
            onView(withText(textId))
                    .check(matches(isDisplayed()))
                    .perform(click())

    fun changeSpinnerSelectedItemPosition(position: Int) {
        Espresso.onData(Matchers.anything())
                .atPosition(position)
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click())
    }

    fun matchDisplayedText(textId: Int): ViewInteraction =
        onView(withText(textId))
                .check(matches(isDisplayed()))

    fun matchDisplayedView(viewId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(isDisplayed()))

    fun matchText(viewId: Int, text: String) {
        onView(withId(viewId))
                .check(matches(withText(text)))
    }

    fun matchText(viewId: Int, textId: Int) {
        onView(withId(viewId))
                .check(matches(withText(textId)))
    }

    fun matchHint(viewId: Int, textId: Int) {
        onView(withId(viewId))
                .check(matches(withHint(textId)))
    }

    fun matchSpinnerText(viewId: Int, textId: Int) {
        onView(withId(viewId))
                .check(matches(withSpinnerText(textId)))
    }

    fun matchViewIsDisabled(viewId: Int) {
        onView(withId(viewId))
                .check(matches(not(isEnabled())))
    }

    fun matchViewInEnabled(viewId: Int) {
        onView(withId(viewId))
                .check(matches(isEnabled()))
    }

    fun matchRecyclerViewItemWithText(viewId: Int, text: String): ViewInteraction =
        onView(withId(viewId))
            .check(matches(RecyclerViewMatchers.withItemText(text)))

    fun matchRecyclerItemCount(viewId: Int, itemCount: Int) {
        onView(withId(viewId))
                .check(matches(RecyclerViewMatchers.withItemCount(itemCount)))
    }

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