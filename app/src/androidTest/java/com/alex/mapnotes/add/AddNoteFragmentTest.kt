package com.alex.mapnotes.add

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.replaceText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.withHint
import android.support.test.espresso.matcher.ViewMatchers.isEnabled
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.R
import com.alex.mapnotes.TestActivity
import com.alex.mapnotes.di.appModule
import org.hamcrest.CoreMatchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class AddNoteFragmentTest {

    private val testNoteText = "test note"
    private val emptyNoteText = ""

    @Rule @JvmField
    val activityRule = ActivityTestRule<TestActivity>(TestActivity::class.java)

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
        activityRule.activity.setFragment(AddNoteFragment())
    }

    @Test
    fun shouldDisplayNoteHintForANewNote() {
        onView(withId(R.id.note))
                .check(matches(withHint(R.string.add_note_hint)))
    }

    @Test
    fun shouldChangeAddButtonEnableAfterChangingNoteText() {
        onView(withId(R.id.add))
                .check(matches(not(isEnabled())))

        onView(withId(R.id.note))
                .perform(replaceText(testNoteText))

        onView(withId(R.id.add))
                .check(matches(isEnabled()))
    }

    @Test
    fun shouldNothingChangeAfterClickOnAddButtonWithEmptyNoteText() {
        onView(withId(R.id.note))
                .check(matches(withText(emptyNoteText)))

        onView(withId(R.id.add))
                .perform(click())

        onView(withId(R.id.note))
                .check(matches(withText(emptyNoteText)))
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}