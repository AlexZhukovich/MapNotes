package com.alex.mapnotes.add

import com.alex.mapnotes.base.MvpPresenter
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider

class AddNotePresenter(private val locationProvider: LocationProvider,
                       private val locationFormatter: LocationFormatter) : MvpPresenter<AddNoteView>, AddNoteMvpPresenter {
    private var view: AddNoteView? = null

    override fun onAttach(view: AddNoteView?) {
        this.view = view
        locationProvider.startLocationUpdates()
    }

    override fun getCurrentLocation() {
        locationProvider.addLocationListener { location ->
            view?.displayCurrentLocation(locationFormatter.format(location))
        }
    }

    override fun onDetach() {
        locationProvider.stopLocationUpdates()
        this.view = null
    }
}