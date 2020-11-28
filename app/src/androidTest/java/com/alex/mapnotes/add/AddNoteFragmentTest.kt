package com.alex.mapnotes.add

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.addNoteFragment
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.testScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AddNoteFragmentTest : MockTest() {

    private val testNoteText = "test note"

    override fun setUp() {
        super.setUp()
        prepare(testScope) {
            mockLocationProvider(isLocationAvailable = true)
            mockAuthorizedUser()
        }
        testScreen {
            launch(AddNoteFragment())
        }
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
}