package com.alex.mapnotes.add

import android.location.Location
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.experimental.launch

class AddNotePresenter(
    private val appExecutors: AppExecutors,
    private val locationProvider: LocationProvider,
    private val locationFormatter: LocationFormatter,
    private val userRepository: UserRepository,
    private val notesRepository: NotesRepository
) : AddNoteMvpPresenter {

    private var view: AddNoteView? = null
    private var lastLocation: Location? = null
    private var uid: String? = null

    override fun onAttach(view: AddNoteView?) {
        this.view = view
        locationProvider.startLocationUpdates()

        launch(appExecutors.ioContext) {
            val userResult = userRepository.getCurrentUser()
            if (userResult is Result.Success) {
                uid = userResult.data.uid
            }
        }
    }

    override fun getCurrentLocation() {
        locationProvider.addUpdatableLocationListener { location ->
            view?.displayCurrentLocation(locationFormatter.format(location))
            lastLocation = location
        }
    }

    override fun addNote(text: String) {
        view?.clearNoteText()
        view?.hideKeyboard()
        launch(appExecutors.ioContext) {
            uid?.let {
                val note = Note(lastLocation?.latitude!!, lastLocation?.longitude!!, text, it)
                notesRepository.addNote(note)
            }
        }
    }

    override fun onDetach() {
        locationProvider.stopLocationUpdates()
        this.view = null
    }
}