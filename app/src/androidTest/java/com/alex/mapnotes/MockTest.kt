package com.alex.mapnotes

import androidx.test.espresso.intent.Intents
import androidx.test.rule.GrantPermissionRule
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import org.junit.After
import org.junit.Before
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.inject

open class MockTest : KoinTest {
    val permissionRule: GrantPermissionRule =
            GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION)

    val locationProvider: LocationProvider by inject()
    val userRepository: UserRepository by inject()
    val notesRepository: NotesRepository by inject()

    val testScope: MockTest by lazy { this }

    @Before
    open fun setUp() {
        stopKoin()
        startKoin {
            modules(
                    listOf(
                        testLocationModule, testDataModule, testAppModule
                    )
            )
        }
        Intents.init()
    }

    @After
    open fun tearDown() {
        Intents.release()
    }
}