package com.alex.mapnotes.robots

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import com.alex.mapnotes.R
import com.alex.mapnotes.add.AddNoteFragment
import com.alex.mapnotes.home.HomeActivity

fun addNoteFragment(func: HomeAddNoteRobot.() -> Unit) = HomeAddNoteRobot().apply { func() }

class HomeAddNoteRobot : BaseTestRobot() {

    fun launch() {
        ActivityScenario.launch(HomeActivity::class.java)
                .onActivity {  }

        launchFragmentInContainer<AddNoteFragment>(
            themeResId = R.style.AppTheme
        )
    }

    fun enterNoteText(text: String) {
        enterText(R.id.note, text)
    }

    fun addNote(text: String) {
        enterNoteText(text)
        clickOnView(R.id.add)
    }

    fun isNoteHintDisplayed(textId: Int) {
        isViewHintDisplayed(R.id.note, textId)
    }

    fun isAddButtonEnabled() {
        isViewEnabled(R.id.add)
    }

    fun isAddButtonDisabled() {
        isViewDisabled(R.id.add)
    }

    fun isNoteTextDisplayed(text: String) {
        isViewWithTextDisplayed(R.id.note, text)
    }

    fun isSuccessfullyDisplayedAddNote() {
        isViewHintDisplayed(R.id.note, R.string.add_note_hint)
        isViewDisabled(R.id.add)
    }
}