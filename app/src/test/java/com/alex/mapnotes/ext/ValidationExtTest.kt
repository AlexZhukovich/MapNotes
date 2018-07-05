package com.alex.mapnotes.ext

import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ValidationExtTest {

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
}