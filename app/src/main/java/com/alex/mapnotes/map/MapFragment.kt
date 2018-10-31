package com.alex.mapnotes.map

import androidx.fragment.app.Fragment

interface MapFragment {
    val fragment: Fragment
    var isInteractionMode: Boolean

    fun hasMarkersOnMap(): Boolean

    fun clearAllMarkers()
}