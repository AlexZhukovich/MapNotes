package com.alex.mapnotes.login

import com.alex.mapnotes.data.repository.AuthRepository

class LoginPresenter(private val authRepository: AuthRepository) : LoginMvpPresenter {
    private var view: LoginView? = null

    override fun onAttach(view: LoginView?) {
        this.view = view
    }

    override fun signIn(email: String, password: String) {
        authRepository.signIn(email, password) {
            if (it.isSuccessful) {
                view?.navigateToMapScreen()
            } else {
                view?.displayError("Something went wrong")
            }
        }
    }

    override fun signUp(email: String, password: String) {
        authRepository.signUp(email, password) {
            if (it.isSuccessful) {
                view?.navigateToMapScreen()
            } else {
                view?.displayError("Something went wrong")
            }
        }
    }

    override fun onDetach() {
        this.view = null
    }
}