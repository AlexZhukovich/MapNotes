package com.alex.mapnotes.login.signin

import com.alex.mapnotes.data.repository.UserRepository

class SignInPresenter(private val userRepository: UserRepository) : SignInMvpPresenter {
    private var view: SignInView? = null

    override fun onAttach(view: SignInView?) {
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

    override fun onDetach() {
        this.view = null
    }
}