package com.alex.mapnotes.matchers

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

object RecyclerViewMatchers {
    fun withItemCount(count: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                val recyclerView = view as RecyclerView
                val adapter = recyclerView.adapter
                return adapter?.itemCount == count
            }

            override fun describeTo(description: Description) {
                description.appendText("RecyclerView should have $count items")
            }
        }
    }
}