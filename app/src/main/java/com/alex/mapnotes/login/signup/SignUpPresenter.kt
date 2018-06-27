package com.alex.mapnotes.login.signup

import android.content.Context
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.isValidEmail
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class SignUpPresenter(
    private var context: Context?,
    private val appExecutors: AppExecutors,
    private val userRepository: UserRepository
) : SignUpMvpPresenter {

    private var view: SignUpView? = null

    override fun onAttach(view: SignUpView?) {
        this.view = view
    }

    override fun signUp(name: String, email: String, password: String) {
        if (email.isEmpty() || !email.isValidEmail()) {
            view?.displayError(context?.getString(R.string.error_email_should_be_valid)!!)
            return
        } else if (password.isEmpty()) {
            view?.displayError(context?.getString(R.string.error_password_should_not_be_empty)!!)
            return
        } else if (name.isEmpty()) {
            view?.displayError(context?.getString(R.string.error_name_should_not_be_empty)!!)
            return
        }

        launch(appExecutors.uiContext) {
            val result = userRepository.signUp(email, password)
            when (result) {
                is Result.Success -> {
                    withContext(appExecutors.ioContext) {
                        userRepository.changeUserName(result.data, name)
                    }
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