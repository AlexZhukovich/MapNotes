package com.alex.mapnotes.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.FragmentTestActivity
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.addNoteFragment
import com.alex.mapnotes.robots.prepare
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddNoteFragmentTest : MockTest() {

    private val testNoteText = "test note"
    private val emptyNoteText = ""

    @Rule @JvmField
    val activityRule = ActivityTestRule<FragmentTestActivity>(FragmentTestActivity::class.java)

    @Before
    override fun setUp() {
        super.setUp()
        prepare(testScope) {
            mockLocationProvider(isLocationAvailable = true)
            mockAuthorizedUser()
        }
        activityRule.activity.setFragment(AddNoteFragment())
    }

    @Test
    fun shouldDisplayNoteHintForANewNote() {
        addNoteFragment {
            isNoteHintDisplayed(R.string.add_note_hint)
        }
    }

    @Test
    fun shouldChangeAddButtonEnableAfterChangingNoteText() {
        addNoteFragment {
            isAddButtonDisabled()
            enterNoteText(testNoteText)
            isAddButtonEnabled()
        }
    }

    @Test
    fun shouldNothingChangeAfterClickOnAddButtonWithEmptyNoteText() {
        addNoteFragment {
            isNoteTextDisplayed(emptyNoteText)
            pressAddButton()
            isNoteTextDisplayed(emptyNoteText)
        }
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
}