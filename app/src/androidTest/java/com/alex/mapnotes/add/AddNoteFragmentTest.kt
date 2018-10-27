package com.alex.mapnotes.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.robots.homeScreen
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
    val activityRule = ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java)

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
        activityRule.activity.setFragment(AddNoteFragment())
    }

    @Test
    fun shouldDisplayNoteHintForANewNote() {
        homeScreen {
            isNoteHintDisplayed(R.string.add_note_hint)
        }
    }

    @Test
    fun shouldChangeAddButtonEnableAfterChangingNoteText() {
        homeScreen {
            isAddButtonDisabled()
            enterNoteText(testNoteText)
            isAddButtonEnabled()
        }
    }

    @Test
    fun shouldNothingChangeAfterClickOnAddButtonWithEmptyNoteText() {
        homeScreen {
            isNoteTextDisplayed(emptyNoteText)
            pressAddButton()
            isNoteTextDisplayed(emptyNoteText)
        }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}