package com.alex.mapnotes.login

import com.alex.mapnotes.data.repository.UserRepository

class LoginPresenter(private val userRepository: UserRepository) : LoginMvpPresenter {
    private var view: LoginView? = null

    override fun onAttach(view: LoginView?) {
        this.view = view
    }

    override fun signIn(email: String, password: String) {
        userRepository.signIn(email, password) {
            if (it.isSuccessful) {
                view?.navigateToMapScreen()
            } else {
                view?.displayError("Something went wrong")
            }
        }
    }

    override fun signUp(name: String, email: String, password: String) {
        userRepository.signUp(email, password) {
            if (it.isSuccessful) {
                userRepository.changeUserName(it.result.user, name)
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