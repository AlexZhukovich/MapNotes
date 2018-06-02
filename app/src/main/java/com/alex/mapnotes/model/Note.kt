package com.alex.mapnotes.model

data class Note(val user: String,
                val latitude: Double,
                val longitude: Double,
                val text: String)