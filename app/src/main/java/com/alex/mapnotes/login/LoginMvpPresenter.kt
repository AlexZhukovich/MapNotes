package com.alex.mapnotes.login

import com.alex.mapnotes.base.MvpPresenter

interface LoginMvpPresenter : MvpPresenter<LoginView> {

    fun openSignIn()

    fun openSignUp()
}