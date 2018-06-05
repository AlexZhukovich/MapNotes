package com.alex.mapnotes.search

import com.alex.mapnotes.base.MvpView
import com.alex.mapnotes.model.Note

interface SearchNotesView : MvpView {

    fun displayNote(note: Note)

    fun clearSearchResults()
}