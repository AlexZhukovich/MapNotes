package com.alex.mapnotes.robots

import androidx.test.core.app.ActivityScenario
import com.agoda.kakao.intent.KIntent
import com.alex.mapnotes.R
import com.alex.mapnotes.login.signup.SignUpActivity

fun signUpScreen(func: SignUpScreenRobot.() -> Unit) = SignUpScreenRobot().apply { func() }

class SignUpScreenRobot : BaseTestRobot() {

    fun displayAsEntryPoint() {
        ActivityScenario.launch(SignUpActivity::class.java)
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
        clickOnView(R.id.signUp)
    }

    fun isSuccessfullyLoaded() {
        KIntent {
            hasComponent(SignUpActivity::class.java.name)
        }
    }

    fun isEmailShouldBeValidErrorDisplayed() {
        isTextDisplayed(R.string.error_email_should_be_valid)
    }

    fun isPasswordShouldNotBeEmptyErrorDisplayed() {
        isTextDisplayed(R.string.error_password_should_not_be_empty)
    }

    fun isNameShouldNotBeEmptyErrorDisplayed() {
        isTextDisplayed(R.string.error_name_should_not_be_empty)
    }

    fun isAccountCannotBeCreatedErrorDisplayed() {
        isTextDisplayed(R.string.error_account_cannot_be_created)
    }
}