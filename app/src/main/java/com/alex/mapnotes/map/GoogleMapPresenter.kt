package com.alex.mapnotes.map

import com.alex.mapnotes.base.ScopedPresenter
import com.alex.mapnotes.model.Location
import com.alex.mapnotes.model.Note

class GoogleMapPresenter : ScopedPresenter<MapView>(), MapMvpPresenter {
    private var view: MapView? = null
    private var currentLocation: Location? = null

    override fun onAttach(view: MapView?) {
        super.onAttach(view)
        this.view = view
    }

    override fun handleInteractionMode(isInteractionMode: Boolean) {
        if (!isInteractionMode) {
            view?.animateCamera(currentLocation)
        }
    }

    override fun handleMapNote(note: Note?) {
        note?.let {
            view?.displayNoteOnMap(it)
        }
    }

    override fun handleLocationUpdate(isInteractionMode: Boolean, newLocation: Location) {
        if (!isInteractionMode && newLocation != currentLocation) {
            view?.animateCamera(newLocation)
        }
        currentLocation = newLocation
    }

    override fun onDetach() {
        super.onDetach()
        this.view = null
    }
}