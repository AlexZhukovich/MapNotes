package com.alex.mapnotes

import androidx.test.espresso.intent.Intents
import androidx.test.rule.GrantPermissionRule
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.map.MapFragment
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.koin.standalone.inject
import org.koin.test.KoinTest

open class MockTest : KoinTest {
    val permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    val locationProvider: LocationProvider by inject()
    val userRepository: UserRepository by inject()
    val notesRepository: NotesRepository by inject()
    val mapFragment: MapFragment by inject()

    val testScope: MockTest by lazy { this }

    open fun setUp() {
        loadKoinModules(listOf(testAppModule))
        Intents.init()
    }

    open fun tearDown() {
        stopKoin()
        Intents.release()
    }
}