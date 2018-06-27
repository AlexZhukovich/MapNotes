package com.alex.mapnotes.search

import android.content.Context
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.launch
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.experimental.Job

class SearchNotesPresenter(
    private var context: Context?,
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

    private fun replaceNoteAuthorIdToNameJob(note: Note): Job {
        return kotlinx.coroutines.experimental.launch {
            val userName = userRepository.getHumanReadableName(note.user!!)
            if (userName is Result.Success) {
                note.user = userName.data
            } else {
                note.user = context?.getString(R.string.unknown_user)
            }
        }
    }

    override fun getNotes() = launch(appExecutors.uiContext) {
        view?.clearSearchResults()
        val notes = notesRepository.getNotes { replaceNoteAuthorIdToNameJob(it) }
        when (notes) {
            is Result.Error -> {
                // TODO: display an error
            }
            is Result.Success -> {
                notes.data.forEach {
                    view?.displayNote(it)
                }
            }
        }
    }

    override fun searchNotes(text: String, categoryPosition: Int) {
        view?.clearSearchResults()
        if (text.isEmpty()) {
            getNotes()
            return
        }

        when (categoryPosition) {
            notesSearchCategory -> {
                launch(appExecutors.uiContext) {
                    val notes = notesRepository.getNotesByNoteText(text) { replaceNoteAuthorIdToNameJob(it) }
                    when (notes) {
                        is Result.Error -> {
                            // TODO: display an error
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
                    kotlinx.coroutines.experimental.launch {
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
                                // TODO: display an error
                            }
                        } else {
                            // TODO: display an error
                        }
                    }.join()
                }
            }
        }
    }

    override fun onDetach() {
        this.view = null
    }
}