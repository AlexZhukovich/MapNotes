package com.alex.mapnotes.add

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.alex.mapnotes.R
import com.alex.mapnotes.di.Properties
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_add_note.view.*
import org.koin.android.ext.android.inject
import org.koin.android.ext.android.setProperty

class AddNoteFragment : Fragment(), AddNoteView {
    private val presenter: AddNoteMvpPresenter by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_note, container, false)

        rootView.note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rootView.add.isEnabled = !s.isNullOrEmpty()
            }
        })
        rootView.add.setOnClickListener {
            val text = rootView.note.text.toString()
            if (text.isNotEmpty()) {
                presenter.addNote(text)
            }
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        setProperty(Properties.ADD_FRAGMENT_CONTEXT, this.context!!)
        presenter.onAttach(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getCurrentLocation()
    }

    override fun clearNoteText() {
        note.text.clear()
    }

    override fun displayCurrentLocation(address: String) {
        currentLocation.text = address
    }

    override fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(note.windowToken, 0)
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }
}