package com.alex.mapnotes.home

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.AuthUser
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomePresenterTest {
    private val addItemId = R.id.navigation_add_note
    private val mapItemId = R.id.navigation_map
    private val searchItemId = R.id.navigation_search_notes
    private val incorrectItemId = -1

    private val view: HomeView = mockk(relaxed = true)
    private val appExecutors: AppExecutors = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)

    private val presenter by lazy { HomePresenter(appExecutors, userRepository) }

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

    // non-null is attached

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
        every { appExecutors.uiContext } returns Dispatchers.Unconfined
        every { appExecutors.ioContext } returns Dispatchers.Unconfined
    }

    @Test
    fun `verify handleNavigationItemClick when non-null view is attached and itemId is add`() {
        every { view.updateMapInteractionMode(true) } answers { nothing }
        every { view.updateNavigationState(BottomSheetBehavior.STATE_COLLAPSED) } answers { nothing }

        presenter.onAttach(view)
        val result = presenter.handleNavigationItemClick(addItemId)

        assertTrue(result)
        verify { view.updateMapInteractionMode(true) }
        verify { view.displayAddNote() }
        verify { view.updateNavigationState(BottomSheetBehavior.STATE_COLLAPSED) }
    }

    @Test
    fun `verify handleNavigationItemClick when non-null view is attached and itemId is map`() {
        every { view.updateNavigationState(BottomSheetBehavior.STATE_HIDDEN) } answers { nothing }

        presenter.onAttach(view)
        val result = presenter.handleNavigationItemClick(mapItemId)

        assertTrue(result)
        verify { view.updateNavigationState(BottomSheetBehavior.STATE_HIDDEN) }
    }

    @Test
    fun `verify handleNavigationItemClick when non-null view is attached and itemId is search`() {
        every { view.updateMapInteractionMode(true) } answers { nothing }
        every { view.updateNavigationState(BottomSheetBehavior.STATE_EXPANDED) } answers { nothing }

        presenter.onAttach(view)
        val result = presenter.handleNavigationItemClick(searchItemId)

        assertTrue(result)
        verify { view.updateMapInteractionMode(true) }
        verify { view.displaySearchNotes() }
        verify { view.updateNavigationState(BottomSheetBehavior.STATE_EXPANDED) }
    }

    @Test
    fun `verify handleNavigationItemClick when non-null view is attached and itemId is incorrect`() {
        expectedException.expect(IllegalArgumentException::class.java)
        expectedException.expectMessage("Unknown itemId")

        presenter.onAttach(view)
        presenter.handleNavigationItemClick(incorrectItemId)

        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify showLocationPermissionRationale when non-null view is attached`() {
        presenter.onAttach(view)
        presenter.showLocationPermissionRationale()

        verify { view.showPermissionExplanationSnackBar() }
        verify { view.hideContentWhichRequirePermissions() }
    }

    @Test
    fun `verify checkUser when non-null view is attached and repository returns success`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser("1111111"))

        presenter.onAttach(view)
        presenter.checkUser()

        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    @Test
    fun `verify checkUser when non-null view is attached and repository returns error`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException())

        presenter.onAttach(view)
        presenter.checkUser()

        verify { view.navigateToLoginScreen() }
    }

    @Test
    fun `verify signOut when non-null view is attached`() {
        presenter.onAttach(view)
        presenter.signOut()

        coVerify { userRepository.signOut() }
        verify { view.navigateToLoginScreen() }
    }

    // null view

    @Test
    fun `verify handleNavigationItemClick when null view is attached and itemId is add`() {
        presenter.onAttach(null)
        val result = presenter.handleNavigationItemClick(addItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify handleNavigationItemClick when null view is attached and itemId is map`() {
        presenter.onAttach(null)
        val result = presenter.handleNavigationItemClick(mapItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify handleNavigationItemClick when null view is attached and itemId is search`() {
        presenter.onAttach(null)
        val result = presenter.handleNavigationItemClick(searchItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify handleNavigationItemClick when null view is attached and itemId is incorrect`() {
        presenter.onAttach(null)
        val result = presenter.handleNavigationItemClick(incorrectItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify showLocationPermissionRationale when null view is attached`() {
        presenter.onAttach(null)
        presenter.showLocationPermissionRationale()

        verify(exactly = 0) { view.showPermissionExplanationSnackBar() }
        verify(exactly = 0) { view.showContentWhichRequirePermissions() }
    }

    @Test
    fun `verify checkUser when null view is attached and repository returns success`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser("1111111"))

        presenter.onAttach(null)
        presenter.checkUser()

        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    @Test
    fun `verify checkUser when null view is attached and repository returns error`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException())

        presenter.onAttach(null)
        presenter.checkUser()

        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    @Test
    fun `verify signOut when null view is attached`() {
        every { view.navigateToLoginScreen() } answers { nothing }
        coEvery { userRepository.signOut() } answers { nothing }

        presenter.onAttach(null)
        presenter.signOut()

        coVerify(exactly = 0) { userRepository.signOut() }
        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    // view is detached

    @Test
    fun `verify handleNavigationItemClick when view is detached and itemId is add`() {
        presenter.onAttach(view)
        presenter.onDetach()
        val result = presenter.handleNavigationItemClick(addItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify handleNavigationItemClick when view is detached and itemId is map`() {
        presenter.onAttach(view)
        presenter.onDetach()
        val result = presenter.handleNavigationItemClick(mapItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify handleNavigationItemClick when view is detached and itemId is search`() {
        presenter.onAttach(view)
        presenter.onDetach()
        val result = presenter.handleNavigationItemClick(searchItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify handleNavigationItemClick when view is detached and itemId is incorrect`() {
        presenter.onAttach(view)
        presenter.onDetach()
        val result = presenter.handleNavigationItemClick(incorrectItemId)

        assertFalse(result)
        verify(exactly = 0) { view.updateNavigationState(any()) }
        verify(exactly = 0) { view.displayAddNote() }
        verify(exactly = 0) { view.displaySearchNotes() }
        verify(exactly = 0) { view.updateMapInteractionMode(any()) }
    }

    @Test
    fun `verify showLocationPermissionRationale when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.showLocationPermissionRationale()

        verify(exactly = 0) { view.showPermissionExplanationSnackBar() }
        verify(exactly = 0) { view.showContentWhichRequirePermissions() }
    }

    @Test
    fun `verify checkUser when view is detached and repository returns success`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Success(AuthUser("1111111"))

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.checkUser()

        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    @Test
    fun `verify checkUser when view is detached and repository returns error`() {
        coEvery { userRepository.getCurrentUser() } returns Result.Error(RuntimeException())

        presenter.onAttach(view)
        presenter.onDetach()
        presenter.checkUser()

        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    @Test
    fun `verify signOut when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.signOut()

        coVerify(exactly = 0) { userRepository.signOut() }
        verify(exactly = 0) { view.navigateToLoginScreen() }
    }

    @After
    fun tearDown() = stopKoin()
}