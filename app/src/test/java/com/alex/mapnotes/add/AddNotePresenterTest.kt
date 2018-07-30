package com.alex.mapnotes.add

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.AuthUser
import io.mockk.mockk
import io.mockk.every
import io.mockk.coEvery
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
class AddNotePresenterTest {
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

        every { appExecutors.ioContext } returns UI
        every { locationProvider.startLocationUpdates() } answers { nothing }
        every { locationProvider.stopLocationUpdates() } answers { nothing }
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser("111111"))
    }

    @Test
    fun `verify startLocationUpdates when non-null view is attached`() {
        presenter.onAttach(view)

        verify { locationProvider.startLocationUpdates() }
    }

    @Test
    fun `verify startLocationUpdates doesn't call when null view is attached`() {
        presenter.onAttach(null)

        verify(exactly = 0) { locationProvider.startLocationUpdates() }
    }

    @Test
    fun `verify stopLocationUpdates when non-null view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()

        verify { locationProvider.stopLocationUpdates() }
    }

    @Test
    fun `verify stopLocationUpdates doesn't call when null view is detached`() {
        presenter.onAttach(null)
        presenter.onDetach()

        verify(exactly = 0) { locationProvider.stopLocationUpdates() }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}