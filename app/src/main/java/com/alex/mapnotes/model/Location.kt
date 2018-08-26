package com.alex.mapnotes.model

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Location(
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
) : Parcelable