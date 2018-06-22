package com.alex.mapnotes.add

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.experimental.launch

class AddNotePresenter(private val appExecutors: AppExecutors,
                       private val locationProvider: LocationProvider,
                       private val locationFormatter: LocationFormatter,
                       private val userRepository: UserRepository,
                       private val notesRepository: NotesRepository) : AddNoteMvpPresenter {
    private var view: AddNoteView? = null

    override fun onAttach(view: AddNoteView?) {
        this.view = view
        locationProvider.startLocationUpdates()
    }

    override fun getCurrentLocation() {
        locationProvider.addUpdatableLocationListener { location ->
            view?.displayCurrentLocation(locationFormatter.format(location))
        }
    }

    override fun addNote(text: String) {
        view?.clearNoteText()
        view?.hideKeyboard()
        locationProvider.addSingleLocationListener {
            launch(appExecutors.uiContext) {
                val currentUser = userRepository.getCurrentUser()
                if (currentUser is Result.Success) {
                    val uid = currentUser.data.uid
                    val note = Note(it.latitude, it.longitude, text, uid)
                    notesRepository.addNote(note)
                }
            }
        }
    }

    override fun onDetach() {
        locationProvider.stopLocationUpdates()
        this.view = null
    }
}