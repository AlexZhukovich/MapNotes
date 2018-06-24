package com.alex.mapnotes.data.repository

import com.alex.mapnotes.data.Result
import com.alex.mapnotes.model.Note
import kotlinx.coroutines.experimental.Job

interface NotesRepository {

    suspend fun addNote(note: Note)

    suspend fun getNotes(replaceAuthorName: (Note) -> Job) : Result<List<Note>>

    suspend fun getNotesByNoteText(text: String, replaceAuthorName: (Note) -> Job) : Result<List<Note>>

    suspend fun getNotesByUser(userId: String, humanReadableName: String) : Result<List<Note>>
}