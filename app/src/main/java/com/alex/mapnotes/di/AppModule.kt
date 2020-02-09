package com.alex.mapnotes.di

import android.content.Context
import android.location.Geocoder
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.add.AddNoteMvpPresenter
import com.alex.mapnotes.add.AddNotePresenter
import com.alex.mapnotes.data.formatter.FullAddressFormatter
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.AddressLocationProvider
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.FirebaseNotesRepository
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.home.HomeMvpPresenter
import com.alex.mapnotes.home.HomePresenter
import com.alex.mapnotes.login.signin.SignInMvpPresenter
import com.alex.mapnotes.login.signin.SignInPresenter
import com.alex.mapnotes.login.signup.SignUpMvpPresenter
import com.alex.mapnotes.login.signup.SignUpPresenter
import com.alex.mapnotes.map.GeneralMapFragment
import com.alex.mapnotes.map.GoogleMapPresenter
import com.alex.mapnotes.map.MapFragment
import com.alex.mapnotes.map.MapMvpPresenter
import com.alex.mapnotes.search.SearchNotesMvpPresenter
import com.alex.mapnotes.search.SearchNotesPresenter
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val locationModule = module(override = true) {
    factory { (activityContext: Context) -> Geocoder(activityContext) }
    factory<LocationProvider> { (activityContext: Context) -> AddressLocationProvider(activityContext) }
    factory<LocationFormatter> { (activityContext: Context) ->
        FullAddressFormatter(
                geocoder = get { parametersOf(activityContext) }
        )
    }
}

val dataModule = module(override = true) {
    factory<UserRepository> {
        FirebaseUserRepository(
                appExecutors = get()
        )
    }
    factory<NotesRepository> {
        FirebaseNotesRepository(
                appExecutors = get()
        )
    }
}

val appModule = module(override = true) {
    single { AppExecutors() }
}

val loginScreenModule = module(override = true) {
    factory<SignInMvpPresenter> {
        SignInPresenter(
                appExecutors = get(),
                userRepository = get()
        )
    }
    factory<SignUpMvpPresenter> {
        SignUpPresenter(
                appExecutors = get(),
                userRepository = get()
        )
    }
}

val homeScreenModule = module(override = true) {
    factory<HomeMvpPresenter> {
        HomePresenter(
                appExecutors = get(),
                userRepository = get()
        )
    }
}

val mapModule = module(override = true) {
    factory<MapFragment> { GeneralMapFragment() }
    factory<MapMvpPresenter> { GoogleMapPresenter() }
}

val addNoteScreenModule = module(override = true) {
    factory<AddNoteMvpPresenter> { (activityContext: Context) ->
        AddNotePresenter(
                appExecutors = get(),
                locationProvider = get { parametersOf(activityContext) },
                locationFormatter = get { parametersOf(activityContext) },
                userRepository = get(),
                notesRepository = get()
        )
    }
}

val searchNotesScreenModule = module(override = true) {
    factory<SearchNotesMvpPresenter> {
        SearchNotesPresenter(
                appExecutors = get(),
                userRepository = get(),
                notesRepository = get()
        )
    }
}