package com.alex.mapnotes.data.repository

import com.alex.mapnotes.data.Result
import com.alex.mapnotes.model.Note
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.experimental.Job

interface NotesRepository {

    fun addNote(note: Note)

    suspend fun getNotes(replaceAuthorName: (Note) -> Job) : Result<List<Note>>

    fun getNotesByNoteText(text: String, listener: ValueEventListener)

    fun getNotesByUser(userId: String, listener: ValueEventListener)
}