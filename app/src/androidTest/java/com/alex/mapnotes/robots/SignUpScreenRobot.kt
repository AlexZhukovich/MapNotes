package com.alex.mapnotes.robots

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.alex.mapnotes.login.signup.SignUpActivity

fun signUpScreen(func: SignUpScreenRobot.() -> Unit) = SignUpScreenRobot().apply { func() }

class SignUpScreenRobot {

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(SignUpActivity::class.java.name))
    }
}