package com.alex.mapnotes.login

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test

class LoginPresenterTest {

    private val view: LoginView = mockk()
    private val presenter by lazy { LoginPresenter() }

    @Test
    fun `verify openSignIn with attached non-null view`() {
        every { view.navigateToSignIn() } answers { nothing }

        presenter.onAttach(view)
        presenter.openSignIn()

        verify { view.navigateToSignIn() }
    }

    @Test
    fun `verify openSignIn with attached null view`() {
        every { view.navigateToSignIn() } answers { nothing }

        presenter.onAttach(null)
        presenter.openSignIn()

        verify(exactly = 0) { view.navigateToSignIn() }
    }

    @Test
    fun `verify openSignIn with detachedView`() {
        every { view.navigateToSignIn() } answers { nothing }

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.openSignIn()

        verify(exactly = 0) { view.navigateToSignIn() }
    }

    @Test
    fun `verify openSignUp with attached non-null view`() {
        every { view.navigateToSignUp() } answers { nothing }

        presenter.onAttach(view)
        presenter.openSignUp()

        verify { view.navigateToSignUp() }
    }

    @Test
    fun `verify openSignUp with attached null view`() {
        every { view.navigateToSignUp() } answers { nothing }

        presenter.onAttach(null)
        presenter.openSignUp()

        verify(exactly = 0) { view.navigateToSignUp() }
    }

    @Test
    fun `verify openSignUp with detachedView`() {
        every { view.navigateToSignUp() } answers { nothing }

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.openSignUp()

        verify(exactly = 0) { view.navigateToSignUp() }
    }
}