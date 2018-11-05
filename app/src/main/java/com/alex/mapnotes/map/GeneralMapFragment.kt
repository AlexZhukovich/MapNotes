package com.alex.mapnotes.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alex.mapnotes.R

class GeneralMapFragment : Fragment(), MapFragment {
    private val googleMapFragment by lazy { GoogleMapFragment() }

    override var isInteractionMode: Boolean
        get() = googleMapFragment.isInteractionMode
        set(value) { googleMapFragment.isInteractionMode = value }

    override val fragment: Fragment
        get() = this

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_map, container, false)

        activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.map, googleMapFragment)
                ?.commit()

        return rootView
    }

    override fun hasMarkersOnMap(): Boolean {
        return googleMapFragment.markers.isNotEmpty()
    }

    override fun clearAllMarkers() {
        googleMapFragment.clearAllMarkers()
    }
}