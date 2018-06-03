package com.alex.mapnotes.add

interface AddNoteMvpPresenter {

    fun getCurrentLocation()

    fun addNote(text: String)
}