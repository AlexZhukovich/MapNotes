package com.alex.mapnotes.login

class LoginPresenter : LoginMvpPresenter {
    private var view: LoginView? = null

    override fun onAttach(view: LoginView?) {
        this.view = view
    }

    override fun openSignIn() {
        view?.navigateToSignIn()
    }

    override fun openSignUp() {
        view?.navigateToSignUp()
    }

    override fun onDetach() {
        this.view = null
    }
}