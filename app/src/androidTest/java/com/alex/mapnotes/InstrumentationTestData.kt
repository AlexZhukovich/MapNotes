package com.alex.mapnotes

import com.alex.mapnotes.model.AuthUser

object InstrumentationTestData {
    const val CORRECT_EMAIL = "test@test.com"
    const val INCORRECT_EMAIL = "test"
    const val EMPTY_EMAIL = ""
    const val PASSWORD = "password"
    const val EMPTY_PASSWORD = ""

    val AUTH_USER = AuthUser("111111")
}