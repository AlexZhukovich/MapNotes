package com.alex.mapnotes.home

import android.support.design.widget.BottomSheetBehavior
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.R
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class HomePresenterTest {
    private val addItemId = R.id.navigation_add_note
    private val mapItemId = R.id.navigation_map
    private val searchItemId = R.id.navigation_search_notes
    private val incorrectItemId = -1

    private val appExecutors: AppExecutors = mockk()
    private val userRepository: UserRepository = mockk()
    private val view: HomeView = mockk()

    private val presenter by lazy { HomePresenter(appExecutors, userRepository) }

    @Rule @JvmField
    val expectedException: ExpectedException = ExpectedException.none()

    // non-null is attached

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
    }

    @Test
    fun `verify handleNavigationItemClick when non-null view is attached and itemId is add`() {
        every { view.updateMapInteractionMode(true) } answers { nothing }
        every { view.displayAddNote() } answers { nothing }
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
        every { view.displaySearchNotes() } answers { nothing }
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
        every { view.showPermissionExplanationSnackBar() } answers { nothing }
        every { view.hideContentWhichRequirePermissions() } answers { nothing }

        presenter.onAttach(view)
        presenter.showLocationPermissionRationale()

        verify { view.showPermissionExplanationSnackBar() }
        verify { view.hideContentWhichRequirePermissions() }
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

    @After
    fun tearDown() = closeKoin()
}