package com.alex.mapnotes.login

import com.alex.mapnotes.base.MvpView

interface LoginView : MvpView {
    fun navigateToMapScreen()

    fun displayError(text: String)
}