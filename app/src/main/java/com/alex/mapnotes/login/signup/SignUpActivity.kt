package com.alex.mapnotes.login.signup

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.ext.clearAndNavigateTo
import com.alex.mapnotes.home.HomeActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.koin.android.ext.android.inject

class SignUpActivity : AppCompatActivity(), SignUpView {
    private val presenter: SignUpMvpPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signUp.setOnClickListener {
            val nameValue = name.text.toString()
            val emailValue = email.text.toString().trim()
            val passwordValue = password.text.toString()
            presenter.signUp(nameValue, emailValue, passwordValue)
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
        Snackbar.make(signUpRoot, R.string.error_email_should_be_valid, Snackbar.LENGTH_LONG).show()
    }

    override fun displayPasswordError() {
        Snackbar.make(signUpRoot, R.string.error_password_should_not_be_empty, Snackbar.LENGTH_LONG).show()
    }

    override fun displaySignUpError() {
        Snackbar.make(signUpRoot, R.string.error_account_cannot_be_created, Snackbar.LENGTH_LONG).show()
    }

    override fun displayEmptyUserNameError() {
        Snackbar.make(signUpRoot, R.string.error_name_should_not_be_empty, Snackbar.LENGTH_LONG).show()
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }
}