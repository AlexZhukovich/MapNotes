package com.alex.mapnotes.robots

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.alex.mapnotes.R
import com.alex.mapnotes.login.LoginActivity

fun loginScreen(func: LoginScreenRobot.() -> Unit) = LoginScreenRobot().apply { func() }

class LoginScreenRobot : BaseTestRobot() {

    fun openSignIn() = clickOnView(R.id.signIn)

    fun openSignUp() = clickOnView(R.id.signUp)

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(LoginActivity::class.java.name))
    }
}