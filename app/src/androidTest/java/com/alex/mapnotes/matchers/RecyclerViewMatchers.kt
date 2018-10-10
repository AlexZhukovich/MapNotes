package com.alex.mapnotes.matchers

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.alex.mapnotes.R
import com.alex.mapnotes.search.adapter.NoteViewHolder
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

    fun withItemText(text: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                val recyclerView = view as RecyclerView
                val adapter = recyclerView.adapter
                val itemCount = adapter?.itemCount!!
                for (pos in 0..itemCount) {
                    val viewHolder = recyclerView.findViewHolderForAdapterPosition(pos) as NoteViewHolder
                    val noteTextView = viewHolder.itemView.findViewById<TextView>(R.id.noteText)
                    if (noteTextView.text.toString() == text) {
                        return true
                    }
                }
                return false
            }

            override fun describeTo(description: Description) {
                description.appendText("RecyclerView should have item with text: $text")
            }
        }
    }
}