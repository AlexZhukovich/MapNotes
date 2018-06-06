package com.alex.mapnotes.map

import android.location.Location
import com.alex.mapnotes.base.MvpPresenter
import com.alex.mapnotes.model.Note

interface MapMvpPresenter : MvpPresenter<MapView> {

    fun handleInteractionMode(isInteractionMode: Boolean)

    fun handleMapNote(note: Note?)

    fun handleLocationUpdate(isInteractionMode: Boolean, newLocation: Location)
}