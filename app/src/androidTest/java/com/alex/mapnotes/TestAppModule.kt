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
import org.koin.dsl.module

val testLocationModule = module(override = true) {
    single<LocationProvider> { mockk() }
    single<LocationFormatter> { mockk() }
}

val testDataModule = module(override = true) {
    single<UserRepository>(override = true) { mockk() }
    single<NotesRepository>(override = true) { mockk() }
}

val testAppModule = module {
    single(override = true) { AppExecutors() }
    factory<MapFragment>(override = true) { FakeMapFragment() }
    factory<MapMvpPresenter>(override = true) { GoogleMapPresenter() }
    factory<SignInMvpPresenter>(override = true) {
        SignInPresenter(
                appExecutors = get(),
                userRepository = get()
        )
    }
    factory<SignUpMvpPresenter>(override = true) {
        SignUpPresenter(
                appExecutors = get(),
                userRepository = get()
        )
    }
    factory<HomeMvpPresenter>(override = true) {
        HomePresenter(
                appExecutors = get(),
                userRepository = get()
        )
    }
    factory<AddNoteMvpPresenter>(override = true) {
        AddNotePresenter(
                appExecutors = get(),
                locationProvider = get(),
                locationFormatter = get(),
                userRepository = get(),
                notesRepository = get()
        )
    }
    factory<SearchNotesMvpPresenter>(override = true) {
        SearchNotesPresenter(
                appExecutors = get(),
                userRepository = get(),
                notesRepository = get()
        )
    }
}