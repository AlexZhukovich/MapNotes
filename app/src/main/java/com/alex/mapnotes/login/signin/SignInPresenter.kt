package com.alex.mapnotes.login.signin

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.base.ScopedPresenter
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.ext.isValidEmail
import kotlinx.coroutines.experimental.launch

class SignInPresenter(
    private val appExecutors: AppExecutors,
    private val userRepository: UserRepository
) : ScopedPresenter<SignInView>(), SignInMvpPresenter {

    private var view: SignInView? = null

    override fun onAttach(view: SignInView?) {
        super.onAttach(view)
        this.view = view
    }

    override fun signIn(email: String, password: String) {
        view?.let {
            if (email.isEmpty() || !email.isValidEmail()) {
                it.displayEmailError()
                return
            } else if (password.isEmpty()) {
                it.displayPasswordError()
                return
            }

            launch(appExecutors.networkContext) {

                val result = userRepository.signIn(email, password)
                when (result) {
                    is Result.Success -> {
                        it.navigateToMapScreen()
                    }
                    is Result.Error -> {
                        it.displaySignInError()
                    }
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        this.view = null
    }
}