package com.alex.mapnotes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alex.mapnotes.map.MapFragment

class FakeMapFragment : Fragment(), MapFragment {
    override val fragment: Fragment
        get() = this
    override var isInteractionMode: Boolean
        get() = true
        set(_) {}

    override fun hasMarkersOnMap(): Boolean {
        return false
    }

    override fun clearAllMarkers() { }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
}