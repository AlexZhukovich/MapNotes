package com.alex.mapnotes.login.signin

import com.alex.mapnotes.base.MvpPresenter

interface SignInMvpPresenter : MvpPresenter<SignInView> {

    fun signIn(email: String, password: String)
}