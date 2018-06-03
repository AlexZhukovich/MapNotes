package com.alex.mapnotes.data.repository

import com.alex.mapnotes.model.Note
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class FirebaseNotesRepository : NotesRepository {
    private val notesPath = "notes"

    private val database by lazy { FirebaseDatabase.getInstance() }

    override fun addNote(note: Note) {
        val notesRef = database.getReference(notesPath)
        val key: String = notesRef.push().key!!
        notesRef.child(key).setValue(note)
    }

    override fun getNotes(listener: ValueEventListener) {
        database.getReference(notesPath).addListenerForSingleValueEvent(listener)
    }
}