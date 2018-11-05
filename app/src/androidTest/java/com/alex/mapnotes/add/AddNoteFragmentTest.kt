package com.alex.mapnotes.add

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.R
import com.alex.mapnotes.robots.addNoteFragment
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.testFragmentActivity
import com.alex.mapnotes.robots.testScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddNoteFragmentTest : MockTest() {

    private val testNoteText = "test note"

    @Rule @JvmField
    val activityRule = testFragmentActivity

    @Before
    override fun setUp() {
        super.setUp()
        prepare(testScope) {
            mockLocationProvider(isLocationAvailable = true)
            mockAuthorizedUser()
        }
        testScreen { attachFragment(AddNoteFragment()) }
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

    @After
    override fun tearDown() {
        super.tearDown()
    }
}