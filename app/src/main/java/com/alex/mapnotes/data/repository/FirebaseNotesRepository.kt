package com.alex.mapnotes.data.repository

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.model.Note
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.withContext
import kotlin.coroutines.experimental.suspendCoroutine

class FirebaseNotesRepository(private val appExecutors: AppExecutors) : NotesRepository {
    private val notesPath = "notes"
    private val textKey = "text"
    private val userKey = "user"

    private val database by lazy { FirebaseDatabase.getInstance() }

    override fun addNote(note: Note) {
        val notesRef = database.getReference(notesPath)
        val newNoteRef = notesRef.push()
        newNoteRef.setValue(note)
    }

    override suspend fun getNotes(replaceAuthorName: (Note) -> Job): Result<List<Note>> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<List<Note>>> {
            database.getReference(notesPath).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    it.resume(Result.Error(databaseError.toException()))
                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    async(appExecutors.networkContext) {

                        if (dataSnapshot.exists()) {
                            val noteResults = mutableListOf<Note>()
                            dataSnapshot.children.forEach({
                                val note = it.getValue(Note::class.java)!!
                                replaceAuthorName(note).join()
                                noteResults.add(note)
                            })
                            it.resume(Result.Success(noteResults))
                        }
                    }
                }
            })
        }
    }

    override suspend fun getNotesByNoteText(text: String, replaceAuthorName: (Note) -> Job): Result<List<Note>> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<List<Note>>> {
            database.getReference(notesPath)
                    .orderByChild(textKey)
                    .startAt(text)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            it.resume(Result.Error(databaseError.toException()))
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            async(appExecutors.networkContext) {
                                if (dataSnapshot.exists()) {
                                    val noteResults = mutableListOf<Note>()
                                    dataSnapshot.children.forEach({
                                        val note = it.getValue(Note::class.java)!!
                                        replaceAuthorName(note).join()
                                        noteResults.add(note)
                                    })
                                    it.resume(Result.Success(noteResults))
                                }
                            }
                        }
                    })
        }
    }

    override suspend fun getNotesByUser(userId: String, humanReadableName: String): Result<List<Note>> = withContext(appExecutors.networkContext) {
        suspendCoroutine<Result<List<Note>>> {
            database.getReference(notesPath)
                    .orderByChild(userKey)
                    .equalTo(userId)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(databaseError: DatabaseError) {
                            it.resume(Result.Error(databaseError.toException()))
                        }

                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                val noteResults = mutableListOf<Note>()
                                dataSnapshot.children.forEach({
                                    val note = it.getValue(Note::class.java)!!
                                    note.user = humanReadableName
                                    noteResults.add(note)
                                })
                                it.resume(Result.Success(noteResults))
                            }
                        }
                    })
        }
    }
}