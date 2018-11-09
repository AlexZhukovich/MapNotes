package com.alex.mapnotes.add

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.AuthUser
import com.alex.mapnotes.model.Location
import com.alex.mapnotes.model.Note
import io.mockk.mockk
import io.mockk.every
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.koin.standalone.StandAloneContext.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AddNotePresenterTest {
    private val uid = "111111"
    private val sydneyLocation = Location(-33.8688, 151.2093)
    private val testNoteText = "test note"
    private val sydneyTestNote = Note(sydneyLocation.latitude, sydneyLocation.longitude, testNoteText, uid)

    private val view: AddNoteView = mockk()
    private val appExecutors: AppExecutors = mockk()
    private val userRepository: UserRepository = mockk()
    private val notesRepository: NotesRepository = mockk()
    private val locationProvider: LocationProvider = mockk()
    private val locationFormatter: LocationFormatter = mockk()

    private val presenter by lazy {
        AddNotePresenter(appExecutors, locationProvider, locationFormatter, userRepository, notesRepository)
    }

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))

        every { appExecutors.ioContext } returns Dispatchers.Main
        every { view.clearNoteText() } answers { nothing }
        every { view.hideKeyboard() } answers { nothing }
        every { locationProvider.startLocationUpdates() } answers { nothing }
        every { locationProvider.stopLocationUpdates() } answers { nothing }
        coEvery { notesRepository.addNote(any()) } answers { nothing }
    }

    @Test
    fun `verify startLocationUpdates when non-null view is attached`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.onAttach(view)

        verify { locationProvider.startLocationUpdates() }
    }

    @Test
    fun `verify startLocationUpdates doesn't call when null view is attached`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.onAttach(null)

        verify(exactly = 0) { locationProvider.startLocationUpdates() }
    }

    @Test
    fun `verify addNote method with attached non-null view and authorized user`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.updateLastLocation(sydneyLocation)

        presenter.onAttach(view)
        presenter.addNote(testNoteText)

        verify { view.clearNoteText() }
        verify { view.hideKeyboard() }
        coVerify { notesRepository.addNote(sydneyTestNote) }
    }

    @Test
    fun `verify addNote method with attached non-null view and non-authorized user`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException("Auth Error"))

        presenter.updateLastLocation(sydneyLocation)

        presenter.onAttach(view)
        presenter.addNote(testNoteText)

        verify { view.clearNoteText() }
        verify { view.hideKeyboard() }
        coVerify(exactly = 0) { notesRepository.addNote(any()) }
    }

    @Test
    fun `verify addNote method with attached null view and authorized user`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.updateLastLocation(sydneyLocation)

        presenter.onAttach(null)
        presenter.addNote(testNoteText)

        verify(exactly = 0) { view.clearNoteText() }
        verify(exactly = 0) { view.hideKeyboard() }
        coVerify(exactly = 0) { notesRepository.addNote(any()) }
    }

    @Test
    fun `verify addNote method with attached null view and non-authorized user`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException("Auth Error"))

        presenter.updateLastLocation(sydneyLocation)

        presenter.onAttach(null)
        presenter.addNote(testNoteText)

        verify(exactly = 0) { view.clearNoteText() }
        verify(exactly = 0) { view.hideKeyboard() }
        coVerify(exactly = 0) { notesRepository.addNote(any()) }
    }

    @Test
    fun `verify addNote method with detached view and authorized user`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.onAttach(view)

        presenter.updateLastLocation(sydneyLocation)
        presenter.onDetach()
        presenter.addNote(testNoteText)

        verify(exactly = 0) { view.clearNoteText() }
        verify(exactly = 0) { view.hideKeyboard() }
        coVerify(exactly = 0) { notesRepository.addNote(any()) }
    }

    @Test
    fun `verify addNote method with detached view and non-authorized user`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException("Auth Error"))

        presenter.updateLastLocation(sydneyLocation)

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.addNote(testNoteText)

        verify(exactly = 0) { view.clearNoteText() }
        verify(exactly = 0) { view.hideKeyboard() }
        coVerify(exactly = 0) { notesRepository.addNote(any()) }
    }

    @Test
    fun `verify stopLocationUpdates when non-null view is detached`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.onAttach(view)
        presenter.onDetach()

        verify { locationProvider.stopLocationUpdates() }
    }

    @Test
    fun `verify stopLocationUpdates doesn't call when null view is detached`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser(uid))

        presenter.onAttach(null)
        presenter.onDetach()

        verify(exactly = 0) { locationProvider.stopLocationUpdates() }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}