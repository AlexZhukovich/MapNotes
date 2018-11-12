package com.alex.mapnotes.di

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
import com.alex.mapnotes.map.MapFragment
import com.alex.mapnotes.map.GoogleMapPresenter
import com.alex.mapnotes.map.GeneralMapFragment
import com.alex.mapnotes.map.MapMvpPresenter
import com.alex.mapnotes.search.SearchNotesMvpPresenter
import com.alex.mapnotes.search.SearchNotesPresenter
import org.koin.dsl.module.module

val locationModule = module(override = true) {
    factory { Geocoder(getProperty(Properties.FRAGMENT_CONTEXT)) }

    factory { AddressLocationProvider(getProperty(Properties.FRAGMENT_CONTEXT)) as LocationProvider }

    factory { FullAddressFormatter(get()) as LocationFormatter }
}

val dataModule = module(override = true) {
    factory { FirebaseUserRepository(get()) as UserRepository }

    factory { FirebaseNotesRepository(get()) as NotesRepository }
}

val appModule = module(override = true) {
    single { AppExecutors() }
}

val loginScreenModule = module(override = true) {
    factory { SignInPresenter(get(), get()) as SignInMvpPresenter }

    factory { SignUpPresenter(get(), get()) as SignUpMvpPresenter }
}

val homeScreenModule = module(override = true) {
    factory { HomePresenter(get(), get()) as HomeMvpPresenter }
}

val mapModule = module(override = true) {
    factory { GeneralMapFragment() as MapFragment }

    factory { GoogleMapPresenter() as MapMvpPresenter }
}

val addNoteScreenModule = module(override = true) {
    factory { AddNotePresenter(get(), get(), get(), get(), get()) as AddNoteMvpPresenter }
}

val searchNotesScreenModule = module(override = true) {
    factory { SearchNotesPresenter(get(), get(), get()) as SearchNotesMvpPresenter }
}

object Properties {
    const val FRAGMENT_CONTEXT = "FRAGMENT_CONTEXT"
}