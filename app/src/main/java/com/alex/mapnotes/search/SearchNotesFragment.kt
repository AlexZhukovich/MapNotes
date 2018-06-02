package com.alex.mapnotes.search

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alex.mapnotes.R
import com.alex.mapnotes.model.Note
import kotlinx.android.synthetic.main.fragment_search_notes.view.*

class SearchNotesFragment: Fragment() {

    private lateinit var adapter: NotesAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_search_notes, container, false)

        adapter = NotesAdapter {
            Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
        }
        adapter.setNotes(listOf(
                Note("Alex", -33.8688, 151.2093, "This is awesome city"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city2"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city3"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city4"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city5"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city6"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city7"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city8"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city9"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city10"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city11"),
                Note("Alex", -33.8688, 151.2093, "This is awesome city12")
        ))

        rootView.recyclerView.layoutManager = LinearLayoutManager(activity)
        rootView.recyclerView.itemAnimator = DefaultItemAnimator()
        rootView.recyclerView.adapter = adapter


        return rootView
    }
}
