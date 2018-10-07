package com.alex.mapnotes.login.signin

import com.alex.mapnotes.base.MvpView

interface SignInView : MvpView {
    fun navigateToMapScreen()

    fun displayEmailError()

    fun displayPasswordError()

    fun displaySignInError()
}