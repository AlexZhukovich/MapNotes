package com.alex.mapnotes.robots

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.onData
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
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.withItemCount
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.hasItemTitle
import com.alex.mapnotes.matchers.BottomNavigationViewMatchers.hasCheckedItem
import com.alex.mapnotes.matchers.RecyclerViewMatchers
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers

open class BaseTestRobot {

    fun enterText(viewId: Int, text: String): ViewInteraction =
            onView(withId(viewId))
                    .perform(replaceText(text), closeSoftKeyboard())

    fun clickOnView(viewId: Int): ViewInteraction =
            onView(withId(viewId))
                    .perform(click())

    fun clickOnViewWithText(textId: Int): ViewInteraction =
            onView(withText(textId))
                    .check(matches(isDisplayed()))
                    .perform(click())

    fun changeSelectedSpinnerItemPosition(position: Int): ViewInteraction =
            onData(Matchers.anything())
                    .atPosition(position)
                    .inRoot(RootMatchers.isPlatformPopup())
                    .perform(click())

    fun isTextDisplayed(textId: Int): ViewInteraction =
            onView(withText(textId))
                    .check(matches(isDisplayed()))

    fun isViewDisplayed(viewId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(isDisplayed()))

    fun isViewWithTextDisplayed(viewId: Int, text: String): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(withText(text)))

    fun isViewWithTextDisplayed(viewId: Int, textId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(withText(textId)))

    fun isViewHintDisplayed(viewId: Int, textId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(withHint(textId)))

    fun isSpinnerHasText(viewId: Int, textId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(withSpinnerText(textId)))

    fun isViewEnabled(viewId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(isEnabled()))

    fun isViewDisabled(viewId: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(not(isEnabled())))

    fun isRecyclerViewHasItemWithText(viewId: Int, text: String): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(RecyclerViewMatchers.withItemText(text)))

    fun isRecyclerViewItemCount(viewId: Int, itemCount: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(RecyclerViewMatchers.withItemCount(itemCount)))

    fun isBottomNavigationItemCount(viewId: Int, itemsCount: Int): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(withItemCount(itemsCount)))

    fun isBottomNavigationHasItemTitle(viewId: Int, title: String): ViewInteraction =
            onView(withId(viewId))
                    .check(matches(hasItemTitle(title)))

    fun isBottomNavigationHasCheckedItemId(navigationViewId: Int, itemViewId: Int): ViewInteraction =
            onView(withId(navigationViewId))
                    .check(matches(hasCheckedItem(itemViewId)))
}