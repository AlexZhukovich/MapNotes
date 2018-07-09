package com.alex.mapnotes.home

import com.alex.mapnotes.base.MvpPresenter

interface HomeMvpPresenter : MvpPresenter<HomeView> {

    fun handleNavigationItemClick(itemId: Int): Boolean

    fun showLocationPermissionRationale()

    fun checkUser()

    fun signOut()
}