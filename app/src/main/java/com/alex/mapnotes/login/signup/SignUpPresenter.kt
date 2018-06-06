package com.alex.mapnotes.login.signup

import android.content.Context
import android.util.Patterns
import com.alex.mapnotes.R
import com.alex.mapnotes.data.repository.UserRepository

class SignUpPresenter(private var context: Context?,
                      private val userRepository: UserRepository) : SignUpMvpPresenter {
    private var view: SignUpView? = null

    override fun onAttach(view: SignUpView?) {
        this.view = view
    }

    override fun signUp(name: String, email: String, password: String) {
        if (email.isEmpty() || !isValidEmail(email)) {
            view?.displayError(context?.getString(R.string.error_email_should_be_valid)!!)
            return
        } else if (password.isEmpty()) {
            view?.displayError(context?.getString(R.string.error_password_should_not_be_empty)!!)
            return
        } else if (name.isEmpty()) {
            view?.displayError(context?.getString(R.string.error_name_should_not_be_empty)!!)
            return
        }

        userRepository.signUp(email, password) {
            if (it.isSuccessful) {
                userRepository.changeUserName(it.result.user, name)
                view?.navigateToMapScreen()
            } else {
                view?.displayError("Something went wrong")
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val matcher = Patterns.EMAIL_ADDRESS.matcher(email)
        return matcher.matches()
    }

    override fun onDetach() {
        this.view = null
        this.context = null
    }
}