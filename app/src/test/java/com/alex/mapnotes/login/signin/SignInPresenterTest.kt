package com.alex.mapnotes.login.signin

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.TestData
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.exception.UserNotAuthenticatedException
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SignInPresenterTest {

    private val view: SignInView = mockk(relaxed = true)
    private val appExecutors: AppExecutors = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val presenter by lazy { SignInPresenter(appExecutors, userRepository) }

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))

        every { appExecutors.networkContext } returns Dispatchers.Main
    }

    @Test
    fun `verify singIn with correct login and incorrect password, non-null view attached to presenter`() {
        coEvery { userRepository.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD) } returns Result.Error(UserNotAuthenticatedException())

        presenter.onAttach(view)
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD)

        verify { view.displaySignInError() }
    }

    @Test
    fun `verify singIn with correct login and incorrect password, null view attached to presenter`() {
        coEvery { userRepository.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD) } returns Result.Error(UserNotAuthenticatedException())

        presenter.onAttach(null)
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD)

        verify(exactly = 0) { view.displaySignInError() }
    }

    @Test
    fun `verify singIn with correct login and incorrect password, view detached to presenter`() {
        coEvery { userRepository.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD) } returns Result.Error(UserNotAuthenticatedException())

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD)

        verify(exactly = 0) { view.displaySignInError() }
    }

    @Test
    fun `verify signIn with correct login and password, non-null view attached to presenter`() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Success(TestData.AUTH_USER)

        presenter.onAttach(view)
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD)

        verify { view.navigateToMapScreen() }
    }

    @Test
    fun `verify signIn with correct login and password, null view attached to presenter`() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Success(TestData.AUTH_USER)

        presenter.onAttach(null)
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD)

        verify(exactly = 0) { view.navigateToMapScreen() }
    }

    @Test
    fun `verify signIn with correct login and password, view detached from presenter`() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Success(TestData.AUTH_USER)

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.PASSWORD)

        verify(exactly = 0) { view.navigateToMapScreen() }
    }

    @Test
    fun `verify sinIn with empty login and password, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signIn(TestData.EMPTY_EMAIL, TestData.EMPTY_PASSWORD)

        verify { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with empty login and password, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signIn(TestData.EMPTY_EMAIL, TestData.EMPTY_PASSWORD)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with empty login and password, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signIn(TestData.EMPTY_EMAIL, TestData.EMPTY_PASSWORD)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with incorrect login and empty password, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signIn(TestData.INCORRECT_EMAIL, TestData.EMPTY_PASSWORD)

        verify { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with incorrect login and empty password, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signIn(TestData.INCORRECT_EMAIL, TestData.EMPTY_PASSWORD)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with incorrect login and empty password, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signIn(TestData.INCORRECT_EMAIL, TestData.EMPTY_PASSWORD)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with correct login and empty password, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.EMPTY_PASSWORD)

        verify { view.displayPasswordError() }
    }

    @Test
    fun `verify sinIn with correct login and empty password, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.EMPTY_PASSWORD)

        verify(exactly = 0) { view.displayPasswordError() }
    }

    @Test
    fun `verify sinIn with correct login and empty password, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signIn(TestData.CORRECT_EMAIL, TestData.EMPTY_PASSWORD)

        verify(exactly = 0) { view.displayPasswordError() }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}