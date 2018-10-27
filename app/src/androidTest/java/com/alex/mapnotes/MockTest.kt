package com.alex.mapnotes

import androidx.test.rule.GrantPermissionRule
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.inject
import org.koin.test.KoinTest

open class MockTest : KoinTest {
    val permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    val locationProvider: LocationProvider by inject()
    val userRepository: UserRepository by inject()
    val notesRepository: NotesRepository by inject()

    val testScope: MockTest by lazy { this }

    open fun setUp() {
        loadKoinModules(listOf(testAppModule))
    }

    open fun tearDown() {
        closeKoin()
    }
}