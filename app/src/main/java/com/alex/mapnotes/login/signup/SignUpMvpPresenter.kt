package com.alex.mapnotes.login.signup

import com.alex.mapnotes.base.MvpPresenter

interface SignUpMvpPresenter : MvpPresenter<SignUpView> {

    fun signUp(name: String, email: String, password: String)
}