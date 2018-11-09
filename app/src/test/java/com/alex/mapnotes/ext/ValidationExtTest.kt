package com.alex.mapnotes.ext

import com.alex.mapnotes.di.appModule
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ValidationExtTest {

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
    }

    @Test
    fun `verify isValidEmail with correct email`() {
        val email = "test@test.com"

        assertTrue(email.isValidEmail())
    }

    @Test
    fun `verify isValidEmail with incorrect email`() {
        val email = "test"

        assertFalse(email.isValidEmail())
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}