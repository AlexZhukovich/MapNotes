package com.alex.mapnotes.add

import androidx.annotation.VisibleForTesting
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.base.ScopedPresenter
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.model.Location
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.experimental.launch

class AddNotePresenter(
    private val appExecutors: AppExecutors,
    private val locationProvider: LocationProvider,
    private val locationFormatter: LocationFormatter,
    private val userRepository: UserRepository,
    private val notesRepository: NotesRepository
) : ScopedPresenter<AddNoteView>(), AddNoteMvpPresenter {

    private var view: AddNoteView? = null
    private var lastLocation: Location? = null
    private var uid: String? = null

    @VisibleForTesting
    fun updateLastLocation(location: Location) {
        lastLocation = location
    }

    override fun onAttach(view: AddNoteView?) {
        super.onAttach(view)
        this.view = view
        view?.let {
            locationProvider.startLocationUpdates()
            launch(appExecutors.ioContext) {
                val userResult = userRepository.getCurrentUser()
                if (userResult is Result.Success) {
                    uid = userResult.data.uid
                }
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
        view?.let { view ->
            view.clearNoteText()
            view.hideKeyboard()
            launch(appExecutors.ioContext) {
                uid?.let { uid ->
                    val note = Note(lastLocation?.latitude!!, lastLocation?.longitude!!, text, uid)
                    notesRepository.addNote(note)
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        view?.let {
            locationProvider.stopLocationUpdates()
            this.view = null
        }
    }
}