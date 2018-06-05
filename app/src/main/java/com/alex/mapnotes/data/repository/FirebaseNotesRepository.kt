package com.alex.mapnotes.data.repository

import com.alex.mapnotes.model.Note
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseNotesRepository : NotesRepository {
    private val notesPath = "notes"

    private val database by lazy { FirebaseDatabase.getInstance() }

    override fun addNote(note: Note) {
        val notesRef = database.getReference(notesPath)
        val newNoteRef = notesRef.push()
        newNoteRef.setValue(note)
    }

    override fun getNotes(listener: ValueEventListener) {
        database.getReference(notesPath).addListenerForSingleValueEvent(listener)
    }

    override fun getNotesByNoteText(text: String, listener: ValueEventListener) {
        database.getReference(notesPath)
                .orderByChild("text")
                .startAt(text)
                .addListenerForSingleValueEvent(listener)
    }

    override fun getNotesByUser(userId: String, listener: ValueEventListener) {
        database.getReference(notesPath)
                .orderByChild("user")
                .equalTo(userId)
                .addListenerForSingleValueEvent(listener)
    }
}