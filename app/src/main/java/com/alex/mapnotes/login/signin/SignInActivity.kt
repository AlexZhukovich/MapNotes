package com.alex.mapnotes.login.signin

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.ext.clearAndNavigateTo
import com.alex.mapnotes.home.HomeActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.koin.android.ext.android.inject

class SignInActivity : AppCompatActivity(), SignInView {
    private val presenter: SignInMvpPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signIn.setOnClickListener {
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString()
            presenter.signIn(emailValue, passwordValue)
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
    }

    override fun navigateToMapScreen() {
        clearAndNavigateTo(HomeActivity::class.java)
    }

    override fun displayEmailError() {
        Snackbar.make(signInRoot, R.string.error_email_should_be_valid, Snackbar.LENGTH_SHORT).show()
    }

    override fun displayPasswordError() {
        Snackbar.make(signInRoot, R.string.error_password_should_not_be_empty, Snackbar.LENGTH_SHORT).show()
    }

    override fun displaySignInError(message: String) {
        Snackbar.make(signInRoot, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }
}