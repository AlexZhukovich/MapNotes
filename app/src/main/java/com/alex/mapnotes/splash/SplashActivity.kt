package com.alex.mapnotes.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.alex.mapnotes.MainActivity
import com.alex.mapnotes.data.repository.FirebaseAuthRepository
import com.alex.mapnotes.login.LoginActivity
import com.alex.mapnotes.navigation.Navigator

class SplashActivity : AppCompatActivity() {
    private val authRepository by lazy { FirebaseAuthRepository() }
    private val navigator by lazy { Navigator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isAuthenticated()) {
            navigator.navigateTo(MainActivity::class.java)
        } else {
            navigator.navigateTo(LoginActivity::class.java)
        }
        finish()
    }

    private fun isAuthenticated() : Boolean {
        return authRepository.getUser() != null
    }
}