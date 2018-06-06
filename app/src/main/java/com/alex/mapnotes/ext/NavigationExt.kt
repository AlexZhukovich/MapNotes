package com.alex.mapnotes.ext

import android.app.Activity
import android.content.Intent

fun Activity.navigateTo(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}

fun Activity.clearAndNavigateTo(cls: Class<*>) {
    val intent = Intent(this, cls).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    startActivity(intent)
}