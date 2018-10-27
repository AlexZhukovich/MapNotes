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
        if (email.isNotEmpty()) {
            enterText(R.id.email, email)
        }
        if (password.isNotEmpty()) {
            enterText(R.id.password, password)
        }
        clickOnView(R.id.signIn)
    }

    fun isSignInErrorDisplayed() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_user_cannot_be_authenticated)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        isTextDisplayed(R.string.error_user_cannot_be_authenticated)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    fun isEmptyPasswordErrorDisplayed() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_password_should_not_be_empty)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        isTextDisplayed(R.string.error_password_should_not_be_empty)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    fun isIncorrectEmailErrorDisplayed() {
        val snackbarErrorTextIdlingResource = ViewTextIdlingResource(
                com.google.android.material.R.id.snackbar_text,
                R.string.error_email_should_be_valid)
        IdlingRegistry.getInstance().register(snackbarErrorTextIdlingResource)

        isTextDisplayed(R.string.error_email_should_be_valid)

        IdlingRegistry.getInstance().unregister(snackbarErrorTextIdlingResource)
    }

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(SignInActivity::class.java.name))
    }
}