package com.alex.mapnotes.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(var latitude: Double = 0.0,
                var longitude: Double = 0.0,
                var text: String? = null,
                var user: String? = null) : Parcelable