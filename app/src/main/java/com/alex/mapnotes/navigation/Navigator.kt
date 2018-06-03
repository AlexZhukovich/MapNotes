package com.alex.mapnotes.navigation

import android.content.Context
import android.content.Intent

class Navigator(private val context: Context) {

    fun navigateTo(cls: Class<*>) {
        val intent = Intent(context, cls)
        context.startActivity(intent)
    }
}