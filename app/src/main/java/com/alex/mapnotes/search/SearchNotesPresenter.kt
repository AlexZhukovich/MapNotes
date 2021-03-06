package com.alex.mapnotes.search

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.base.ScopedPresenter
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.IllegalArgumentException

class SearchNotesPresenter(
    private val appExecutors: AppExecutors,
    private val userRepository: UserRepository,
    private val notesRepository: NotesRepository
) : ScopedPresenter<SearchNotesView>(), SearchNotesMvpPresenter {

    private var view: SearchNotesView? = null
    private val notesSearchCategory = 0
    private val usersSearchCategory = 1

    override fun onAttach(view: SearchNotesView?) {
        super.onAttach(view)
        this.view = view
    }

    private fun replaceNoteAuthorIdToNameJob(note: Note, defaultUserName: String): Job {
        return launch {
            val userName = userRepository.getHumanReadableName(note.user!!)
            if (userName is Result.Success) {
                note.user = userName.data
            } else {
                note.user = defaultUserName
            }
        }
    }

    override fun getNotes(defaultUserName: String) {
        view?.let { notesView ->
            notesView.clearSearchResults()
            launch(appExecutors.uiContext) {
                val notes = notesRepository.getNotes { replaceNoteAuthorIdToNameJob(it, defaultUserName) }
                when (notes) {
                    is Result.Error -> {
                        notesView.displayLoadingNotesError()
                    }
                    is Result.Success -> {
                        notes.data.forEach {
                            notesView.displayNote(it)
                        }
                    }
                }
            }
        }
    }

    override fun searchNotes(text: String, categoryPosition: Int, defaultUserName: String) {
        view?.let { notesView ->
            notesView.clearSearchResults()
            if (text.isEmpty()) {
                getNotes(defaultUserName)
                return
            }

            when (categoryPosition) {
                notesSearchCategory -> {
                    launch(appExecutors.uiContext) {
                        val notes = notesRepository.getNotesByNoteText(text) { replaceNoteAuthorIdToNameJob(it, defaultUserName) }
                        when (notes) {
                            is Result.Error -> {
                                notesView.displayLoadingNotesError()
                            }
                            is Result.Success -> {
                                notes.data.forEach {
                                    notesView.displayNote(it)
                                }
                            }
                        }
                    }
                }
                usersSearchCategory -> {
                    launch(appExecutors.networkContext) {
                        withContext(appExecutors.networkContext) {
                            val userId = userRepository.getUserIdFromHumanReadableName(text)
                            if (userId is Result.Success) {
                                val notes = notesRepository.getNotesByUser(userId.data, text)
                                if (notes is Result.Success) {
                                    notes.data.forEach {
                                        launch(appExecutors.uiContext) {
                                            notesView.displayNote(it)
                                        }
                                    }
                                } else {
                                    notesView.displayLoadingNotesError()
                                }
                            } else {
                                notesView.displayUnknownUserError()
                            }
                        }
                    }
                }
                else -> throw IllegalArgumentException("Incorrect ID of category")
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.view = null
    }
}