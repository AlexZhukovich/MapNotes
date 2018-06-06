package com.alex.mapnotes.login

import com.alex.mapnotes.base.MvpView

interface LoginView : MvpView {
    fun navigateToSignIn()

    fun navigateToSignUp()
}