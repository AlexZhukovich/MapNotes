package com.alex.mapnotes.search

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.alex.mapnotes.R
import com.alex.mapnotes.data.formatter.CoordinateFormatter
import com.alex.mapnotes.data.formatter.LatLonFormatter
import com.alex.mapnotes.home.DISPLAY_LOCATION
import com.alex.mapnotes.home.EXTRA_NOTE
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.search.adapter.NotesAdapter
import kotlinx.android.synthetic.main.fragment_search_notes.*
import kotlinx.android.synthetic.main.fragment_search_notes.view.*
import org.koin.android.ext.android.inject

class SearchNotesFragment : Fragment(), SearchNotesView {
    private val defaultUserName by lazy { getString(R.string.unknown_user) }
    private val coordinateFormatter: LatLonFormatter by lazy { CoordinateFormatter() }
    private lateinit var adapter: NotesAdapter

    private val presenter: SearchNotesMvpPresenter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_search_notes, container, false)
        rootView.searchOptions.adapter = ArrayAdapter.createFromResource(
                activity,
                R.array.search_options,
                android.R.layout.simple_dropdown_item_1line)
        adapter = NotesAdapter(coordinateFormatter) {
            val broadcastManager = LocalBroadcastManager.getInstance(this.context!!)
            val intent = Intent(DISPLAY_LOCATION)
            intent.apply {
                putExtra(EXTRA_NOTE, it)
            }
            broadcastManager.sendBroadcast(intent)
        }
        val layoutManager = LinearLayoutManager(activity)
        rootView.recyclerView.layoutManager = layoutManager
        rootView.recyclerView.addItemDecoration(
                DividerItemDecoration(rootView.recyclerView.context, layoutManager.orientation))
        rootView.recyclerView.adapter = adapter

        rootView.searchButton.setOnClickListener {
            presenter.searchNotes(rootView.searchText.text.toString(), rootView.searchOptions.selectedItemPosition, defaultUserName)
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getNotes(defaultUserName)
    }

    override fun clearSearchResults() {
        adapter.clear()
    }

    override fun displayNote(note: Note) {
        adapter.addNote(note)
    }

    override fun displayLoadingNotesError() {
        Snackbar.make(recyclerView, R.string.loading_notes_error, Snackbar.LENGTH_LONG).show()
    }

    override fun displayUnknownUserError() {
        Snackbar.make(recyclerView, R.string.unknown_user_error, Snackbar.LENGTH_LONG).show()
    }

    override fun onStop() {
        presenter.toString()
        super.onStop()
    }
}