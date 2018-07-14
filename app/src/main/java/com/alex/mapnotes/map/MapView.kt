package com.alex.mapnotes.map

import com.alex.mapnotes.base.MvpView
import com.alex.mapnotes.model.Location
import com.alex.mapnotes.model.Note

interface MapView : MvpView {

    fun animateCamera(currentLocation: Location?)

    fun displayNoteOnMap(note: Note)
}