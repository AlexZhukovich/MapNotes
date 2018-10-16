package com.alex.mapnotes.add

import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
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
            verifyNoteHint(R.string.add_note_hint)
        }
    }

    @Test
    fun shouldChangeAddButtonEnableAfterChangingNoteText() {
        homeScreen {
            verifyAddButtonIsDisabled()
            enterNoteText(testNoteText)
            verifyAddButtonIsEnabled()
        }
    }

    @Test
    fun shouldNothingChangeAfterClickOnAddButtonWithEmptyNoteText() {
        homeScreen {
            verifyNoteText(emptyNoteText)
            pressAddButton()
            verifyNoteText(emptyNoteText)
        }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}