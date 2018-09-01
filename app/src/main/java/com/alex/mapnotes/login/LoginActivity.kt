package com.alex.mapnotes.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alex.mapnotes.R
import com.alex.mapnotes.ext.navigateTo
import com.alex.mapnotes.login.signin.SignInActivity
import com.alex.mapnotes.login.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginView {
    private val presenter: LoginMvpPresenter by lazy { LoginPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        signIn.setOnClickListener {
            presenter.openSignIn()
        }

        signUp.setOnClickListener {
            presenter.openSignUp()
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

    override fun navigateToSignIn() {
        navigateTo(SignInActivity::class.java)
    }

    override fun navigateToSignUp() {
        navigateTo(SignUpActivity::class.java)
    }
}