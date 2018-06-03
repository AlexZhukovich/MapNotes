package com.alex.mapnotes.login

import com.alex.mapnotes.base.MvpPresenter

interface LoginMvpPresenter : MvpPresenter<LoginView> {

    fun signIn(email: String, password: String)

    fun signUp(email: String, password: String)
}