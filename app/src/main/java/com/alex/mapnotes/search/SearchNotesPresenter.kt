package com.alex.mapnotes.search

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.launch
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.withContext

class SearchNotesPresenter(
    private val userRepository: UserRepository,
    private val notesRepository: NotesRepository,
    private val appExecutors: AppExecutors
) : SearchNotesMvpPresenter {

    private var view: SearchNotesView? = null
    private val notesSearchCategory = 0
    private val usersSearchCategory = 1

    override fun onAttach(view: SearchNotesView?) {
        this.view = view
    }

    private fun replaceNoteAuthorIdToNameJob(note: Note, defaultUserName: String): Job {
        return kotlinx.coroutines.experimental.launch {
            val userName = userRepository.getHumanReadableName(note.user!!)
            if (userName is Result.Success) {
                note.user = userName.data
            } else {
                note.user = defaultUserName
            }
        }
    }

    override fun getNotes(defaultUserName: String) = launch(appExecutors.uiContext) {
        view?.clearSearchResults()
        val notes = notesRepository.getNotes { replaceNoteAuthorIdToNameJob(it, defaultUserName) }
        when (notes) {
            is Result.Error -> {
                view?.displayLoadingNotesError()
            }
            is Result.Success -> {
                notes.data.forEach {
                    view?.displayNote(it)
                }
            }
        }
    }

    override fun searchNotes(text: String, categoryPosition: Int, defaultUserName: String) {
        view?.clearSearchResults()
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
                            view?.displayLoadingNotesError()
                        }
                        is Result.Success -> {
                            notes.data.forEach {
                                view?.displayNote(it)
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
                                        view?.displayNote(it)
                                    }
                                }
                            } else {
                                view?.displayLoadingNotesError()
                            }
                        } else {
                            view?.displayUnknownUserError()
                        }
                    }
                }
            }
        }
    }

    override fun onDetach() {
        this.view = null
    }
}