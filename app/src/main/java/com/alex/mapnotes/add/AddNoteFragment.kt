package com.alex.mapnotes.add

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.alex.mapnotes.R
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_add_note.view.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class AddNoteFragment : Fragment(R.layout.fragment_add_note), AddNoteView {
    private val presenter: AddNoteMvpPresenter by inject { parametersOf(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                add.isEnabled = !s.isNullOrEmpty()
            }
        })

        view.add.setOnClickListener {
            val text = note.text.toString()
            if (text.isNotEmpty()) {
                presenter.addNote(text)
            }
        }
    }

    override fun onStart() {
        super.onStart()
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