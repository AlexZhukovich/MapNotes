package com.alex.mapnotes.login

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alex.mapnotes.MockTest
import com.alex.mapnotes.robots.homeScreen
import com.alex.mapnotes.robots.prepare
import com.alex.mapnotes.robots.signUpActivity
import com.alex.mapnotes.robots.signUpScreen
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SignUpActivityTest : MockTest() {
    private val username = "testUserName"
    private val emptyUsername = ""
    private val incorrectEmail = "test"
    private val correctEmail = "test@test.com"
    private val emptyEmail = ""
    private val password = "password"
    private val emptyPassword = ""

    @Rule
    @JvmField
    val chain: RuleChain = RuleChain
            .outerRule(permissionRule)
            .around(signUpActivity)

    @Before
    override fun setUp() {
        super.setUp()
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsEmpty() {
        signUpScreen {
            displayAsEntryPoint()
            signUp(emptyUsername, emptyEmail, emptyPassword)
            isEmailShouldBeValidErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayEmailErrorWhenEmailIsNotCorrect() {
        signUpScreen {
            displayAsEntryPoint()
            signUp(emptyUsername, incorrectEmail, emptyPassword)
            isEmailShouldBeValidErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayPasswordErrorWhenPasswordIsEmpty() {
        signUpScreen {
            displayAsEntryPoint()
            signUp(emptyUsername, correctEmail, emptyPassword)
            isPasswordShouldNotBeEmptyErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplayNameErrorWhenNameIsEmpty() {
        signUpScreen {
            displayAsEntryPoint()
            signUp(emptyUsername, correctEmail, password)
            isNameShouldNotBeEmptyErrorDisplayed()
        }
    }

    @Test
    fun shouldDisplaySignUpErrorAfterSignUpError() {
        prepare(testScope) {
            mockSignUpError()
        }
        signUpScreen {
            displayAsEntryPoint()
            signUp(username, correctEmail, password)
            isAccountCannotBeCreatedErrorDisplayed()
        }
    }

    @Test
    fun shouldOpenMapScreenAfterSuccessfulSignUp() {
        Intents.init()

        prepare(testScope) {
            mockLocationProvider(isLocationAvailable = false)
            mockSuccessfulSignUp(username, correctEmail, password)
            mockAuthorizedUser()
        }
        signUpScreen {
            displayAsEntryPoint()
            signUp(username, correctEmail, password)
        }
        homeScreen {
            isSuccessfullyLoaded()
        }

        Intents.release()
    }

    @After
    override fun tearDown() {
        super.tearDown()
    }
}