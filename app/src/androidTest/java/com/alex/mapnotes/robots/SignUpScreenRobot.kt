package com.alex.mapnotes.robots

import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers
import androidx.test.rule.ActivityTestRule
import com.alex.mapnotes.login.signup.SignUpActivity
import com.alex.mapnotes.R

fun signUpScreen(func: SignUpScreenRobot.() -> Unit) = SignUpScreenRobot().apply { func() }

val signUpActivity = ActivityTestRule<SignUpActivity>(SignUpActivity::class.java, true, false)

class SignUpScreenRobot : BaseTestRobot() {

    fun display() {
        signUpActivity.launchActivity(null)
    }

    fun signUp(name: String, email: String, password: String) {
        if (name.isNotEmpty()) {
            enterText(R.id.name, name)
        }
        if (email.isNotEmpty()) {
            enterText(R.id.email, email)
        }
        if (password.isNotEmpty()) {
            enterText(R.id.password, password)
        }
        clickView(R.id.signUp)
    }

    fun isSuccessfullyLoaded() {
        Intents.intended(IntentMatchers.hasComponent(SignUpActivity::class.java.name))
    }

    fun isEmailShouldBeValidErrorDisplayed() {
        matchDisplayedText(R.string.error_email_should_be_valid)
    }

    fun isPasswordShouldNotBeEmptyErrorDisplayed() {
        matchDisplayedText(R.string.error_password_should_not_be_empty)
    }

    fun isNameShouldNotBeEmptyErrorDisplayed() {
        matchDisplayedText(R.string.error_name_should_not_be_empty)
    }

    fun isAccountCannotBeCreatedErrorDisplayed() {
        matchDisplayedText(R.string.error_account_cannot_be_created)
    }
}