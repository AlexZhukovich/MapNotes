package com.alex.mapnotes.add

import com.alex.mapnotes.base.MvpView

interface AddNoteView : MvpView {

    fun displayCurrentLocation(address: String)
}