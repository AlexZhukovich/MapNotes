package com.alex.mapnotes.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.alex.mapnotes.data.formatter.LatLonFormatter
import com.alex.mapnotes.model.Note
import kotlinx.android.synthetic.main.item_note.view.*

class NoteViewHolder(
    view: View,
    private val itemClick: (Note) -> Unit,
    private val formatter: LatLonFormatter
) : RecyclerView.ViewHolder(view) {

    fun bindNote(note: Note) {
        with(note) {
            itemView.noteText.text = text
            itemView.noteLocation.text = formatter.format(latitude, longitude)
            itemView.noteUser.text = user

            itemView.setOnClickListener { itemClick(this) }
        }
    }
}