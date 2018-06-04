package com.alex.mapnotes.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.alex.mapnotes.R
import com.alex.mapnotes.data.formatter.LatLonFormatter
import com.alex.mapnotes.model.Note

class NotesAdapter(private val formatter: LatLonFormatter,
                   private val itemClick: (Note) -> Unit) : RecyclerView.Adapter<NoteViewHolder>() {
    private var notes: MutableList<Note> = mutableListOf()

    fun addNote(note: Note) {
        this.notes.add(note)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view, itemClick, formatter)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bindNote(notes[position])
    }

}