package com.alex.mapnotes.robots

import androidx.test.espresso.IdlingRegistry
import com.alex.mapnotes.R
import com.alex.mapnotes.matchers.ViewTextIdlingResource

fun signInScreen(func: SignInRobot.() -> Unit) = SignInRobot().apply { func() }

class SignInRobot : BaseTestRobot() {

    fun enterEmail(email: String) = enterText(R.id.email, email)

    fun enterPassword(password: String) = enterText(R.id.password, password)

    fun pressSignIn() = clickButton(R.id.signIn)

    fun matchSignInErrorMessage() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_user_cannot_be_authenticated)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        matchDisplayedText(R.string.error_user_cannot_be_authenticated)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }
}