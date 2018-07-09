package com.alex.mapnotes.di

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.login.signin.SignInMvpPresenter
import com.alex.mapnotes.login.signin.SignInPresenter
import com.alex.mapnotes.login.signup.SignUpMvpPresenter
import com.alex.mapnotes.login.signup.SignUpPresenter
import org.koin.dsl.module.applicationContext

val appModule = applicationContext {

    bean { AppExecutors() }

    factory { FirebaseUserRepository(get()) as UserRepository }

    factory { SignInPresenter(get(), get()) as SignInMvpPresenter }

    factory { SignUpPresenter(get(), get()) as SignUpMvpPresenter }
}