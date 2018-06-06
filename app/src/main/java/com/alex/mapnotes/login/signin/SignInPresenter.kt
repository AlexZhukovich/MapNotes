package com.alex.mapnotes.login.signin

import android.content.Context
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.R
import com.alex.mapnotes.ext.isValidEmail


class SignInPresenter(private var context: Context?,
                      private val userRepository: UserRepository) : SignInMvpPresenter {
    private var view: SignInView? = null

    override fun onAttach(view: SignInView?) {
        this.view = view
    }

    override fun signIn(email: String, password: String) {
        if (email.isEmpty() || !email.isValidEmail()) {
            view?.displayError(context?.getString(R.string.error_email_should_be_valid)!!)
            return
        } else if (password.isEmpty()) {
            view?.displayError(context?.getString(R.string.error_password_should_not_be_empty)!!)
            return
        }

        userRepository.signIn(email, password) {
            if (it.isSuccessful) {
                view?.navigateToMapScreen()
            } else {
                view?.displayError(it.exception?.message!!)
            }
        }

    }

    override fun onDetach() {
        this.view = null
        this.context = null
    }
}