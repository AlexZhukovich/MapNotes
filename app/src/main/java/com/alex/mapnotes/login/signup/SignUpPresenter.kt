package com.alex.mapnotes.login.signup

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.isValidEmail
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

class SignUpPresenter(
    private val appExecutors: AppExecutors,
    private val userRepository: UserRepository
) : SignUpMvpPresenter {

    private var view: SignUpView? = null

    override fun onAttach(view: SignUpView?) {
        this.view = view
    }

    override fun signUp(name: String, email: String, password: String) {
        view?.let {
            if (email.isEmpty() || !email.isValidEmail()) {
                it.displayEmailError()
                return
            } else if (password.isEmpty()) {
                it.displayPasswordError()
                return
            } else if (name.isEmpty()) {
                it.displayEmptyUserNameError()
                return
            }

            launch(appExecutors.uiContext) {
                val result = userRepository.signUp(email, password)
                when (result) {
                    is Result.Success -> {
                        withContext(appExecutors.ioContext) {
                            userRepository.changeUserName(result.data, name)
                        }
                        it.navigateToMapScreen()
                    }
                    is Result.Error -> {
                        it.displaySignUpError(result.exception.message!!)
                    }
                }
            }
        }
    }

    override fun onDetach() {
        this.view = null
    }
}