package com.alex.mapnotes.login.signin

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.exception.UserNotAuthenticatedException
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.AuthUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.experimental.android.UI
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SignInPresenterTest {

    private val authUserUID = "111111"
    private val authUser = AuthUser(authUserUID)
    private val correctUserName = "test@test.com"
    private val incorrectUserName = "test"
    private val emptyUserName = ""
    private val correctPassword = "password"
    private val incorrectPassword = "incorrect password"
    private val emptyPassword = ""
    private val userNotAuthenticatedErrorMessage = "User not authenticated"

    private val view: SignInView = mockk()
    private val appExecutors: AppExecutors = mockk()
    private val userRepository: UserRepository = mockk()

    private val presenter by lazy { SignInPresenter(appExecutors, userRepository) }

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))

        every { appExecutors.networkContext } returns UI
        every { view.displayEmailError() } answers { nothing }
        every { view.displayPasswordError() } answers { nothing }
        every { view.displaySignInError(userNotAuthenticatedErrorMessage) } answers { nothing }
        every { view.navigateToMapScreen() } answers { nothing }
    }

    @Test
    fun `verify singIn with correct login and incorrect password, non-null view attached to presenter`() {
        coEvery { userRepository.signIn(correctUserName, incorrectPassword) } returns Result.Error(UserNotAuthenticatedException())

        presenter.onAttach(view)
        presenter.signIn(correctUserName, incorrectPassword)

        verify { view.displaySignInError(userNotAuthenticatedErrorMessage) }
    }

    @Test
    fun `verify singIn with correct login and incorrect password, null view attached to presenter`() {
        coEvery { userRepository.signIn(correctUserName, incorrectPassword) } returns Result.Error(UserNotAuthenticatedException())

        presenter.onAttach(null)
        presenter.signIn(correctUserName, incorrectPassword)

        verify(exactly = 0) { view.displaySignInError(userNotAuthenticatedErrorMessage) }
    }

    @Test
    fun `verify singIn with correct login and incorrect password, view detached to presenter`() {
        coEvery { userRepository.signIn(correctUserName, incorrectPassword) } returns Result.Error(UserNotAuthenticatedException())

        presenter.onDetach()
        presenter.signIn(correctUserName, incorrectPassword)

        verify(exactly = 0) { view.displaySignInError(userNotAuthenticatedErrorMessage) }
    }

    @Test
    fun `verify signIn with correct login and password, non-null view attached to presenter`() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Success(authUser)

        presenter.onAttach(view)
        presenter.signIn(correctUserName, correctPassword)

        verify { view.navigateToMapScreen() }
    }

    @Test
    fun `verify signIn with correct login and password, null view attached to presenter`() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Success(authUser)

        presenter.onAttach(null)
        presenter.signIn(correctUserName, correctPassword)

        verify(exactly = 0) { view.navigateToMapScreen() }
    }

    @Test
    fun `verify signIn with correct login and password, view detached from presenter`() {
        coEvery { userRepository.signIn(any(), any()) } returns Result.Success(authUser)

        presenter.onDetach()
        presenter.signIn(correctUserName, correctPassword)

        verify(exactly = 0) { view.navigateToMapScreen() }
    }

    @Test
    fun `verify sinIn with empty login and password, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signIn(emptyUserName, emptyPassword)

        verify { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with empty login and password, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signIn(emptyUserName, emptyPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with empty login and password, view detached from presenter`() {
        presenter.onDetach()
        presenter.signIn(emptyUserName, emptyPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with incorrect login and empty password, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signIn(incorrectUserName, emptyPassword)

        verify { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with incorrect login and empty password, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signIn(incorrectUserName, emptyPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with incorrect login and empty password, view detached from presenter`() {
        presenter.onDetach()
        presenter.signIn(incorrectUserName, emptyPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify sinIn with correct login and empty password, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signIn(correctUserName, emptyPassword)

        verify { view.displayPasswordError() }
    }

    @Test
    fun `verify sinIn with correct login and empty password, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signIn(correctUserName, emptyPassword)

        verify(exactly = 0) { view.displayPasswordError() }
    }

    @Test
    fun `verify sinIn with correct login and empty password, view detached from presenter`() {
        presenter.onDetach()
        presenter.signIn(correctUserName, emptyPassword)

        verify(exactly = 0) { view.displayPasswordError() }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}