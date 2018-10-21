package com.alex.mapnotes.robots

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import com.alex.mapnotes.R
import com.alex.mapnotes.idlingresources.ViewTextIdlingResource
import com.alex.mapnotes.login.signin.SignInActivity

fun signInScreen(func: SignInScreenRobot.() -> Unit) = SignInScreenRobot().apply { func() }

class SignInScreenRobot : BaseTestRobot() {

    fun signIn(email: String, password: String) {
        enterText(R.id.email, email)
        enterText(R.id.password, password)
        clickView(R.id.signIn)
    }

    fun verifySignInErrorMessage() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_user_cannot_be_authenticated)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        matchDisplayedText(R.string.error_user_cannot_be_authenticated)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    fun verifyEmptyPasswordErrorMessage() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_password_should_not_be_empty)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        matchDisplayedText(R.string.error_password_should_not_be_empty)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    fun verifyIncorrectEmailErrorMessage() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_email_should_be_valid)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        matchDisplayedText(R.string.error_email_should_be_valid)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(SignInActivity::class.java.name))
    }
}