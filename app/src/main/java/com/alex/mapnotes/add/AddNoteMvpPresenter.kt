package com.alex.mapnotes.add

import com.alex.mapnotes.base.MvpPresenter

interface AddNoteMvpPresenter : MvpPresenter<AddNoteView> {

    fun getCurrentLocation()

    fun addNote(text: String)
}