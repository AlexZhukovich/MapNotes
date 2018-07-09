package com.alex.mapnotes

import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.login.signin.SignInMvpPresenter
import com.alex.mapnotes.login.signin.SignInPresenter
import com.alex.mapnotes.login.signup.SignUpMvpPresenter
import com.alex.mapnotes.login.signup.SignUpPresenter
import io.mockk.mockk
import org.koin.dsl.module.applicationContext

val testAppModule = applicationContext {

    bean { AppExecutors() }

    bean <UserRepository> { mockk() }

    factory { SignInPresenter(get(), get()) as SignInMvpPresenter }

    factory { SignUpPresenter(get(), get()) as SignUpMvpPresenter }
}