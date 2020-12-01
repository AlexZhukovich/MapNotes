package com.alex.mapnotes.add

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.addNoteFragment
import com.alex.mapnotes.robots.prepare
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class AddNoteFragmentTest : MockTest() {
    private val testNoteText = "test note"

    @Test
    fun shouldDisplayNoteHintForANewNote() {
        prepare(testScope) {
            mockLocationProvider(isLocationAvailable = true)
            mockAuthorizedUser()
        }
        addNoteFragment {
            launch()
            isNoteHintDisplayed(R.string.add_note_hint)
        }
    }

    @Test
    fun shouldChangeAddButtonEnableAfterChangingNoteText() {
        prepare(testScope) {
            mockLocationProvider(isLocationAvailable = true)
            mockAuthorizedUser()
        }
        addNoteFragment {
            launch()
            isAddButtonDisabled()
            enterNoteText(testNoteText)
            isAddButtonEnabled()
        }
    }
}