package com.alex.mapnotes.login

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.alex.mapnotes.MainActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.data.repository.FirebaseAuthRepository
import com.alex.mapnotes.navigation.Navigator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {

    private val authRepository by lazy { FirebaseAuthRepository() }
    private val presenter by lazy { LoginPresenter(authRepository) }
    private val navigator by lazy { Navigator(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signIn.setOnClickListener {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString()
            presenter.signIn(emailValue, passwordValue)
        }

        signUp.setOnClickListener {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString()
            presenter.signUp(emailValue, passwordValue)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }

    override fun navigateToMapScreen() {
        finish()
        navigator.navigateTo(MainActivity::class.java)
    }

    override fun displayError(text: String) {
        Toast.makeText(this@LoginActivity, text, Toast.LENGTH_SHORT).show()
    }
}