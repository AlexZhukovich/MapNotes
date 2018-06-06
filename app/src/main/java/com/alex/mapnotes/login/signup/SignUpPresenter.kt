package com.alex.mapnotes.login.signup

import com.alex.mapnotes.data.repository.UserRepository

class SignUpPresenter(private val userRepository: UserRepository) : SignUpMvpPresenter {
    private var view: SignUpView? = null

    override fun onAttach(view: SignUpView?) {
        this.view = view
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