package com.alex.mapnotes.matchers

import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object BottomNavigationViewMatchers {

    fun hasCheckedItem(itemId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                val bottomNavigationView = view as BottomNavigationView
                val menu = bottomNavigationView.menu
                for (i in 0 until menu.size()) {
                    val item = menu.getItem(i)
                    if (item.isChecked) {
                        return item.itemId == itemId
                    }
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("BottomNavigationView should have checked item with id: $itemId")
            }
        }
    }

    fun withItemCount(count: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                val bottomNavigationView = view as BottomNavigationView
                val menu = bottomNavigationView.menu
                return menu.size() == count
            }

            override fun describeTo(description: Description) {
                description.appendText("BottomNavigationView should have $count item")
            }
        }
    }

    fun hasItemTitle(text: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                val bottomNavigationView = view as BottomNavigationView
                val menu = bottomNavigationView.menu
                for (i in 0 until menu.size()) {
                    val item = menu.getItem(i)
                    if (item.title.contains(text)) {
                        return true
                    }
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("BottomNavigationView should have item with text: $text")
            }
        }
    }
}