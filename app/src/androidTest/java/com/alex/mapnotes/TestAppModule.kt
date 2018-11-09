package com.alex.mapnotes

import com.alex.mapnotes.add.AddNoteMvpPresenter
import com.alex.mapnotes.add.AddNotePresenter
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.home.HomeMvpPresenter
import com.alex.mapnotes.home.HomePresenter
import com.alex.mapnotes.login.signin.SignInMvpPresenter
import com.alex.mapnotes.login.signin.SignInPresenter
import com.alex.mapnotes.login.signup.SignUpMvpPresenter
import com.alex.mapnotes.login.signup.SignUpPresenter
import com.alex.mapnotes.map.MapFragment
import com.alex.mapnotes.map.GoogleMapPresenter
import com.alex.mapnotes.map.MapMvpPresenter
import com.alex.mapnotes.search.SearchNotesMvpPresenter
import com.alex.mapnotes.search.SearchNotesPresenter
import io.mockk.mockk
import org.koin.dsl.module.applicationContext

val testAppModule = applicationContext {

    single(override = true) { AppExecutors() }

    single(override = true) { mockk<LocationProvider>() }

    single(override = true) { mockk<UserRepository>() }

    single(override = true) { mockk<NotesRepository>() }

    single(override = true) { mockk<LocationFormatter>() }

    factory(override = true) { SignInPresenter(get(), get()) as SignInMvpPresenter }

    factory(override = true) { SignUpPresenter(get(), get()) as SignUpMvpPresenter }

    factory(override = true) { HomePresenter(get(), get()) as HomeMvpPresenter }

    factory(override = true) { AddNotePresenter(get(), get(), get(), get(), get()) as AddNoteMvpPresenter }

    factory(override = true) { SearchNotesPresenter(get(), get(), get()) as SearchNotesMvpPresenter }

    factory(override = true) { FakeMapFragment() as MapFragment }

    factory(override = true) { GoogleMapPresenter() as MapMvpPresenter }
}