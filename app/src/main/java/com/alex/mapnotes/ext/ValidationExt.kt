package com.alex.mapnotes.ext

import android.util.Patterns

fun String.isValidEmail() : Boolean {
    val matcher = Patterns.EMAIL_ADDRESS.matcher(this)
    return matcher.matches()
}