package com.alex.mapnotes.map

import android.location.Location
import com.alex.mapnotes.base.MvpView
import com.alex.mapnotes.model.Note

interface MapView : MvpView {

    fun animateCamera(currentLocation: Location?)

    fun displayMoteOnMap(note: Note)
}