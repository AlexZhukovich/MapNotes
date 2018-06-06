package com.alex.mapnotes.search

import android.content.Context
import com.alex.mapnotes.R
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchNotesPresenter(private var context: Context?,
                           private val userRepository: UserRepository,
                           private val notesRepository: NotesRepository) : SearchNotesMvpPresenter {
    private var view: SearchNotesView? = null
    private val notesSearchCategory = 0
    private val usersSearchCategory = 1

    override fun onAttach(view: SearchNotesView?) {
        this.view = view
    }

    override fun getNotes() {
        view?.clearSearchResults()
        notesRepository.getNotes(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children.forEach {
                        val note: Note = it.getValue(Note::class.java)!!
                        userRepository.getHumanReadableName(note.user!!, object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                note.user = context?.getString(R.string.unknown_user)
                                view?.displayNote(note)
                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    note.user = dataSnapshot.children.first().value.toString()
                                    view?.displayNote(note)
                                }
                            }
                        })
                    }
                }
            }
        })
    }

    override fun searchNotes(text: String, categoryPosition: Int) {
        view?.clearSearchResults()
        if (text.isEmpty()) {
            getNotes()
            return
        }

        when (categoryPosition) {
            notesSearchCategory -> {
                notesRepository.getNotesByNoteText(text, object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {
                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            dataSnapshot.children.forEach({
                                val note: Note = it.getValue(Note::class.java)!!
                                userRepository.getHumanReadableName(note.user!!, object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                        note.user = context?.getString(R.string.unknown_user)
                                        view?.displayNote(note)
                                    }

                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            note.user = dataSnapshot.children.first().value.toString()
                                            view?.displayNote(note)
                                        }
                                    }
                                })
                            })
                        }
                    }

                })
            }
            usersSearchCategory -> {
                userRepository.getUserIdFromHumanReadableName(text, object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(userdDataSnapshot: DataSnapshot) {
                        if (userdDataSnapshot.exists()) {
                            val userId = userdDataSnapshot.children.first().key.toString()
                            notesRepository.getNotesByUser(userId, object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                    view?.clearSearchResults()
                                }

                                override fun onDataChange(notesDataSnapshot: DataSnapshot) {
                                    if (notesDataSnapshot.exists()) {
                                        notesDataSnapshot.children.forEach({
                                            val note: Note = it.getValue(Note::class.java)!!
                                            note.user = text
                                            view?.displayNote(note)
                                        })
                                    }
                                }
                            })
                        }
                    }
                })
            }
        }
    }

    override fun onDetach() {
        this.view = null
        this.view = null
    }
}