package com.alex.mapnotes.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.navigateTo
import com.alex.mapnotes.home.HomeActivity
import com.alex.mapnotes.login.LoginActivity
import kotlinx.coroutines.experimental.runBlocking
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val authRepository: UserRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAuthenticated()) {
            navigateTo(HomeActivity::class.java)
        } else {
            navigateTo(LoginActivity::class.java)
        }
        finish()
    }

    private fun isAuthenticated(): Boolean = runBlocking {
        authRepository.getCurrentUser() is Result.Success
    }
}