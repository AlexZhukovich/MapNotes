package com.alex.mapnotes.login

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.robots.loginScreen
import com.alex.mapnotes.robots.signInScreen
import com.alex.mapnotes.robots.signUpScreen
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginActivityTest : MockTest() {

    @Test
    fun shouldLaunchSignInActivityAfterClickToSignInButton() {
        loginScreen {
            launch()
            openSignIn()
        }
        signInScreen {
            isSuccessfullyLoaded()
        }
    }

    @Test
    fun shouldLaunchSignUpActivityAfterClickToSignUpButton() {
        loginScreen {
            launch()
            openSignUp()
        }
        signUpScreen {
            isSuccessfullyLoaded()
        }
    }
}