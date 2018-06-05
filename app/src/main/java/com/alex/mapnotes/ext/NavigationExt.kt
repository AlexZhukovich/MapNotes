package com.alex.mapnotes.ext

import android.app.Activity
import android.content.Intent

fun Activity.navigateTo(cls: Class<*>) {
    val intent = Intent(this, cls)
    startActivity(intent)
}