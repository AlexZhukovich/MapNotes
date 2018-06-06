package com.alex.mapnotes.home

import com.alex.mapnotes.base.MvpView

interface HomeView : MvpView {

    fun updateMapInteractionMode(isInteractionMode: Boolean)

    fun updateNavigationState(newState: Int)

    fun displayAddNote()

    fun displaySearchNotes()

    fun hideContentWhichRequirePermissions()

    fun showContentWhichRequirePermissions()

    fun showPermissionExplanationSnackBar()
}