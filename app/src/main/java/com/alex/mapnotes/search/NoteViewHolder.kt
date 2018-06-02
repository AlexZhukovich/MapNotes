package com.alex.mapnotes.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alex.mapnotes.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteViewHolder(view: View, private val itemClick: (Note) -> Unit) : RecyclerView.ViewHolder(view) {

    fun bindNote(note: Note) {
        with(note) {
            itemView.noteText.text = text
            itemView.noteLocation.text = "($latitude; $longitude)"
            itemView.noteUser.text = user

            itemView.setOnClickListener { itemClick(this) }
        }
    }
}