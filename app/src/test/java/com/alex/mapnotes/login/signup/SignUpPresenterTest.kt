package com.alex.mapnotes.login.signup

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.AuthUser
import io.mockk.every
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SignUpPresenterTest {

    private val authUserUID = "111111"
    private val authUser = AuthUser(authUserUID)
    private val correctUserName = "TestUser"
    private val emptyUserName = ""
    private val correctEmail = "test@test.com"
    private val incorrectEmail = "test"
    private val emptyEmail = ""
    private val correctPassword = "correctPassword"
    private val emptyPassword = ""

    private val errorMessage = "The email address is already in use by another account."

    private val view: SignUpView = mockk()
    private val appExecutors: AppExecutors = mockk()
    private val userRepository: UserRepository = mockk()

    private val presenter by lazy { SignUpPresenter(appExecutors, userRepository) }

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))

        every { appExecutors.uiContext } returns Dispatchers.Main
        every { appExecutors.ioContext } returns Dispatchers.Main

        every { view.displaySignUpError() } answers { nothing }
        every { view.displayEmptyUserNameError() } answers { nothing }
        every { view.displayEmailError() } answers { nothing }
        every { view.displayPasswordError() } answers { nothing }
        every { view.navigateToMapScreen() } answers { nothing }

        coEvery { userRepository.signUp(correctEmail, correctPassword) } returns Result.Success(authUser)
        coEvery { userRepository.changeUserName(authUser, correctUserName) } answers { nothing }
    }

    @Test
    fun `verify signUp with correct email, password and name, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signUp(correctUserName, correctEmail, correctPassword)

        coVerify {
            userRepository.signUp(correctEmail, correctPassword)
            userRepository.changeUserName(authUser, correctUserName)
        }

        verify { view.navigateToMapScreen() }
    }

    @Test
    fun `verify signUp with correct email, password and name, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signUp(correctUserName, correctEmail, correctPassword)

        coVerify(exactly = 0) {
            userRepository.signUp(any(), any())
            userRepository.changeUserName(any(), any())
        }

        verify(exactly = 0) { view.navigateToMapScreen() }
    }

    @Test
    fun `verify signUp with correct email, password and name, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signUp(correctUserName, correctEmail, correctPassword)

        coVerify(exactly = 0) {
            userRepository.signUp(any(), any())
            userRepository.changeUserName(any(), any())
        }

        verify(exactly = 0) { view.navigateToMapScreen() }
    }

    // empty email

    @Test
    fun `verify signUp with empty email, correct password and name, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signUp(correctUserName, emptyEmail, correctPassword)

        verify { view.displayEmailError() }
    }

    @Test
    fun `verify signUp with empty email, correct password and name, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signUp(correctUserName, emptyEmail, correctPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify signUp with empty email, correct password and name, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signUp(correctUserName, emptyEmail, correctPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    // invalid email

    @Test
    fun `verify signUp with incorrect email, password and name, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signUp(correctUserName, incorrectEmail, correctPassword)

        verify { view.displayEmailError() }
    }

    @Test
    fun `verify signUp with incorrect email, password and name, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signUp(correctUserName, incorrectEmail, correctPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    @Test
    fun `verify signUp with incorrect email, password and name, view detached to presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signUp(correctUserName, incorrectEmail, correctPassword)

        verify(exactly = 0) { view.displayEmailError() }
    }

    // empty password

    @Test
    fun `verify signUp with correct email, empty password and name, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signUp(correctUserName, correctEmail, emptyPassword)

        verify { view.displayPasswordError() }
    }

    @Test
    fun `verify signUp with correct email, empty password and name, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signUp(correctUserName, correctEmail, emptyPassword)

        verify(exactly = 0) { view.displayPasswordError() }
    }

    @Test
    fun `verify signUp with correct email, empty password and name, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signUp(correctUserName, correctEmail, emptyPassword)

        verify(exactly = 0) { view.displayPasswordError() }
    }

    // empty name

    @Test
    fun `verify signUp with correct email, correct password and empty name, non-null view attached to presenter`() {
        presenter.onAttach(view)
        presenter.signUp(emptyUserName, correctEmail, correctPassword)

        verify { view.displayEmptyUserNameError() }
    }

    @Test
    fun `verify signUp with correct email, correct password and empty name, null view attached to presenter`() {
        presenter.onAttach(null)
        presenter.signUp(emptyUserName, correctEmail, correctPassword)

        verify(exactly = 0) { view.displayEmptyUserNameError() }
    }

    @Test
    fun `verify signUp with correct email, correct password and empty name, view detached from presenter`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signUp(emptyUserName, correctEmail, correctPassword)

        verify(exactly = 0) { view.displayEmptyUserNameError() }
    }

    // sign up error

    @Test
    fun `verify signUp with correct email, password and name, when server send error, non-null view attached to presenter`() {
        coEvery { userRepository.signUp(correctEmail, correctPassword) } returns Result.Error(Exception(errorMessage))

        presenter.onAttach(view)
        presenter.signUp(correctUserName, correctEmail, correctPassword)

        verify { view.displaySignUpError() }
    }

    @Test
    fun `verify signUp with correct email, password and name, when server send error, null view attached to presenter`() {
        coEvery { userRepository.signUp(correctEmail, correctPassword) } returns Result.Error(Exception(errorMessage))

        presenter.onAttach(null)
        presenter.signUp(correctUserName, correctEmail, correctPassword)

        verify(exactly = 0) { view.displaySignUpError() }
    }

    @Test
    fun `verify signUp with correct email, password and name, when server send error, view detached from presenter`() {
        coEvery { userRepository.signUp(correctEmail, correctPassword) } returns Result.Error(Exception(errorMessage))

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signUp(correctUserName, correctEmail, correctPassword)

        verify(exactly = 0) { view.displaySignUpError() }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}