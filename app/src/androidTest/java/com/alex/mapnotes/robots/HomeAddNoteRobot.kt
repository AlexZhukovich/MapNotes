package com.alex.mapnotes.robots

import com.alex.mapnotes.R

fun addNoteFragment(func: HomeAddNoteRobot.() -> Unit) = HomeAddNoteRobot().apply { func() }

class HomeAddNoteRobot : BaseTestRobot() {

    fun enterNoteText(text: String) = enterText(R.id.note, text)

    fun pressAddButton() = clickOnView(R.id.add)

    fun addNote(text: String) {
        enterNoteText(text)
        clickOnView(R.id.add)
    }

    fun isNoteHintDisplayed(textId: Int) = isViewHintDisplayed(R.id.note, textId)

    fun isAddButtonEnabled() = isViewEnabled(R.id.add)

    fun isAddButtonDisabled() = isViewDisabled(R.id.add)

    fun isNoteTextDisplayed(text: String) = isViewWithTextDisplayed(R.id.note, text)

    fun isSuccessfullyDisplayedAddNote() {
        isViewHintDisplayed(R.id.note, R.string.add_note_hint)
        isViewDisabled(R.id.add)
    }
}