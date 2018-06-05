package com.alex.mapnotes.search

import com.alex.mapnotes.base.MvpPresenter

interface SearchNotesMvpPresenter : MvpPresenter<SearchNotesView> {

    fun getNotes()

    fun searchNotes(text: String, categoryPosition: Int)
}