package com.alex.mapnotes.search

import android.support.test.espresso.Espresso.onData
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.withHint
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.di.appModule
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class SearchNotesFragmentTest {

    @Rule @JvmField
    val activityRule = ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java)

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
        activityRule.activity.setFragment(SearchNotesFragment())
    }

    @Test
    fun shouldVerifyLayout() {
        onView(withId(R.id.recyclerView))
                .check(matches(isDisplayed()))

        onView(withId(R.id.searchText))
                .check(matches(withHint(R.string.search_hint)))

        onView(withId(R.id.searchOptions))
                .check(matches(isDisplayed()))

        onData(allOf(`is`(instanceOf(String::class.java)), `is`("Notes")))
                .check(matches(isDisplayed()))

        onView(withId(R.id.searchButton))
                .check(matches(withText(R.string.search_button_text)))
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}