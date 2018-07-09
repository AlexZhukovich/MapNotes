package com.alex.mapnotes

import android.app.Application
import com.alex.mapnotes.di.appModule
import com.google.android.gms.maps.MapsInitializer
import org.koin.android.ext.android.startKoin

open class MapNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
        MapsInitializer.initialize(this)
    }

    open fun initDI() {
        startKoin(this, listOf(appModule))
    }
}