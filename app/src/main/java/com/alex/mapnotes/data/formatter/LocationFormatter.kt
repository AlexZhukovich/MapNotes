package com.alex.mapnotes.data.formatter

import com.alex.mapnotes.model.Location

interface LocationFormatter {

    fun format(location: Location): String
}