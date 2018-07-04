package com.alex.mapnotes.login.signup

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.clearAndNavigateTo
import com.alex.mapnotes.home.HomeActivity
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), SignUpView {
    private val appExecutors: AppExecutors by lazy { AppExecutors() }
    private val userRepository: UserRepository by lazy { FirebaseUserRepository(appExecutors) }
    private val presenter: SignUpMvpPresenter by lazy { SignUpPresenter(appExecutors, userRepository) }

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
        Toast.makeText(this, R.string.error_email_should_be_valid, Toast.LENGTH_LONG).show()
    }

    override fun displayPasswordError() {
        Toast.makeText(this, R.string.error_password_should_not_be_empty, Toast.LENGTH_LONG).show()
    }

    override fun displaySignUpError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun displayEmptyUserNameError() {
        Toast.makeText(this, R.string.error_name_should_not_be_empty, Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        presenter.onDetach()
        super.onStop()
    }
}