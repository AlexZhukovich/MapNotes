package com.alex.mapnotes.data.formatter

import android.location.Location

interface LocationFormatter {

    fun format(location: Location): String
}