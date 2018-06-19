package com.alex.mapnotes.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.ext.navigateTo
import com.alex.mapnotes.login.LoginActivity
import kotlinx.coroutines.experimental.runBlocking

class SplashActivity : AppCompatActivity() {
    private val appExecutors: AppExecutors by lazy { AppExecutors() }
    private val authRepository by lazy { FirebaseUserRepository(appExecutors) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAuthenticated()) {
            navigateTo(HomeActivity::class.java)
        } else {
            navigateTo(LoginActivity::class.java)
        }
        finish()
    }

    private fun isAuthenticated() : Boolean = runBlocking {
        authRepository.getCurrentUser().await() is Result.Success
    }
}