package com.alex.mapnotes

import android.app.Application
import com.google.android.gms.maps.MapsInitializer

class MapNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        MapsInitializer.initialize(this)
    }
}