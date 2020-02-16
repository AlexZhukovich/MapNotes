package com.alex.mapnotes.map

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alex.mapnotes.R

class GeneralMapFragment : Fragment(R.layout.fragment_map), MapFragment {
    private val googleMapFragment by lazy { GoogleMapFragment() }

    override var isInteractionMode: Boolean
        get() = googleMapFragment.isInteractionMode
        set(value) { googleMapFragment.isInteractionMode = value }

    override val fragment: Fragment
        get() = this

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.map, googleMapFragment)
                ?.commit()
    }

    override fun hasMarkersOnMap(): Boolean {
        return googleMapFragment.markers.isNotEmpty()
    }

    override fun clearAllMarkers() {
        googleMapFragment.clearAllMarkers()
    }
}