package com.alex.mapnotes.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alex.mapnotes.R
import com.alex.mapnotes.data.formatter.CoordinateFormatter
import com.alex.mapnotes.data.repository.FirebaseAuthRepository
import com.alex.mapnotes.data.repository.FirebaseNotesRepository
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.search.adapter.NotesAdapter
import kotlinx.android.synthetic.main.fragment_search_notes.view.*

class SearchNotesFragment: Fragment(), SearchNotesView {
    private lateinit var adapter: NotesAdapter
    private val coordinateFormatter by lazy { CoordinateFormatter() }
    private val authRepository by lazy { FirebaseAuthRepository() }
    private val notesRepository by lazy { FirebaseNotesRepository() }
    private val presenter by lazy { SearchNotesPresenter(authRepository, notesRepository) }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_search_notes, container, false)
        adapter = NotesAdapter(coordinateFormatter) {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
        val layoutManager = LinearLayoutManager(activity)
        rootView.recyclerView.layoutManager = layoutManager
        rootView.recyclerView.itemAnimator = DefaultItemAnimator()
        rootView.recyclerView.addItemDecoration(
                DividerItemDecoration(rootView.recyclerView.context, layoutManager.orientation))
        rootView.recyclerView.adapter = adapter
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
        presenter.getNotes()
    }

    override fun displayNote(note: Note) {
        adapter.addNote(note)
    }

    override fun onStop() {
        presenter.toString()
        super.onStop()
    }
}
