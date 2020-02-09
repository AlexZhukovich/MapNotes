package com.alex.mapnotes.login

import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LoginPresenterTest {

    private val view: LoginView = mockk(relaxed = true)
    private val presenter by lazy { LoginPresenter() }

    @Test
    fun `verify openSignIn with attached non-null view`() {
        presenter.onAttach(view)
        presenter.openSignIn()

        verify { view.navigateToSignIn() }
    }

    @Test
    fun `verify openSignIn with attached null view`() {
        presenter.onAttach(null)
        presenter.openSignIn()

        verify(exactly = 0) { view.navigateToSignIn() }
    }

    @Test
    fun `verify openSignIn with detachedView`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.openSignIn()

        verify(exactly = 0) { view.navigateToSignIn() }
    }

    @Test
    fun `verify openSignUp with attached non-null view`() {
        presenter.onAttach(view)
        presenter.openSignUp()

        verify { view.navigateToSignUp() }
    }

    @Test
    fun `verify openSignUp with attached null view`() {
        presenter.onAttach(null)
        presenter.openSignUp()

        verify(exactly = 0) { view.navigateToSignUp() }
    }

    @Test
    fun `verify openSignUp with detachedView`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.openSignUp()

        verify(exactly = 0) { view.navigateToSignUp() }
    }
}