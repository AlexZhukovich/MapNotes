package com.alex.mapnotes.search

import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchNotesPresenter(private val userRepository: UserRepository,
                           private val notesRepository: NotesRepository) : SearchNotesMvpPresenter {
    private var view: SearchNotesView? = null

    override fun onAttach(view: SearchNotesView?) {
        this.view = view
    }

    override fun getNotes() {
        notesRepository.getNotes(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children.forEach {
                        val note: Note = it.getValue(Note::class.java)!!
                        userRepository.getHumanReadableName(note.user!!, object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {
                                note.user = "Unknown"
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

    override fun onDetach() {
        this.view = null
    }
}