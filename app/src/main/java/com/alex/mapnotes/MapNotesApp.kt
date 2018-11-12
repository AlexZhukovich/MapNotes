package com.alex.mapnotes

import android.app.Application
import com.alex.mapnotes.di.addNoteScreenModule
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.di.dataModule
import com.alex.mapnotes.di.homeScreenModule
import com.alex.mapnotes.di.locationModule
import com.alex.mapnotes.di.loginScreenModule
import com.alex.mapnotes.di.mapModule
import com.alex.mapnotes.di.searchNotesScreenModule
import com.google.android.gms.maps.MapsInitializer
import org.koin.android.ext.android.startKoin

open class MapNotesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
        MapsInitializer.initialize(this)
    }

    open fun initDI() {
        startKoin(this, listOf(
                locationModule,
                dataModule,
                loginScreenModule,
                mapModule,
                homeScreenModule,
                addNoteScreenModule,
                searchNotesScreenModule,
                appModule))
    }
}