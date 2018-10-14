package com.alex.mapnotes.robots

import com.alex.mapnotes.R

fun loginScreen(func: LoginScreenRobot.() -> Unit) = LoginScreenRobot().apply { func() }

class LoginScreenRobot : BaseTestRobot() {

    fun openSignIn() = clickButton(R.id.signIn)
}