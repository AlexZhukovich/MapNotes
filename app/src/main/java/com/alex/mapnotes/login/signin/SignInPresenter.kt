package com.alex.mapnotes.login.signin

import android.content.Context
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.isValidEmail
import kotlinx.coroutines.experimental.launch

class SignInPresenter(private var context: Context?,
                      private val appExecutors: AppExecutors,
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

        launch(appExecutors.networkContext) {

            val result = userRepository.signIn(email, password)
            when (result) {
                is Result.Success -> {
                    view?.navigateToMapScreen()
                }
                is Result.Error -> {
                    view?.displayError(result.exception.message!!)
                }
            }
        }
    }

    override fun onDetach() {
        this.view = null
        this.context = null
    }
}