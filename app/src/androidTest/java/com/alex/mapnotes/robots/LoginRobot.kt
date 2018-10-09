package com.alex.mapnotes.robots

import com.alex.mapnotes.R

fun loginScreen(func: LoginRobot.() -> Unit) = LoginRobot().apply { func() }

class LoginRobot : BaseTestRobot() {

    fun pressSignIn() = clickButton(R.id.signIn)
}