package com.alex.mapnotes.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alex.mapnotes.MainActivity
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.ext.navigateTo
import com.alex.mapnotes.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    private val authRepository by lazy { FirebaseUserRepository() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAuthenticated()) {
            navigateTo(MainActivity::class.java)
        } else {
            navigateTo(LoginActivity::class.java)
        }
        finish()
    }

    private fun isAuthenticated() : Boolean {
        return authRepository.getUser() != null
    }
}