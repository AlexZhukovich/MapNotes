package com.alex.mapnotes.search

import com.alex.mapnotes.data.repository.AuthRepository
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class SearchNotesPresenter(private val authRepository: AuthRepository,
                           private val notesRepository: NotesRepository) : SearchNotesMvpPresenter {
    private var view: SearchNotesView? = null

    override fun onAttach(view: SearchNotesView?) {
        this.view = view
    }

    override fun getNotes() {
        val userId = authRepository.getUser()?.uid
        notesRepository.getNotes(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    dataSnapshot.children.forEach {
                        val item: Note = it.getValue(Note::class.java)!!
                        if (item.user == userId) {
                            item.user = "Me"
                        }
                        view?.displayNote(item)
                    }
                }
            }
        })
    }

    override fun onDetach() {
        this.view = null
    }
}