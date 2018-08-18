package com.alex.mapnotes.map

import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.Location
import com.alex.mapnotes.model.Note
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules

class GoogleMapPresenterTest {

    private val isInteractionMode = true
    private val isNonInteractionMode = false
    private val testNote = Note(text = "test note")
    private val testLocation = Location(10.0, 10.0)

    private val view: MapView = mockk()
    private val presenter by lazy { GoogleMapPresenter() }

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
    }

    // verify a handleInteractionMode method
    @Test
    fun `verify handleInteractionMode with interaction mode when non-null view is attached`() {
        presenter.onAttach(view)
        presenter.handleInteractionMode(isInteractionMode)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleInteractionMode without interaction mode when non-null view is attached`() {
        every { view.animateCamera(any()) } answers { nothing }

        presenter.onAttach(view)
        presenter.handleInteractionMode(isNonInteractionMode)

        verify { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleInteractionMode with interaction mode  when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleInteractionMode(isInteractionMode)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleInteractionMode without interaction mode  when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleInteractionMode(isInteractionMode)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleInteractionMode with interaction mode when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleInteractionMode(isInteractionMode)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleInteractionMode without interaction mode when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleInteractionMode(isNonInteractionMode)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    // verify a handleMapNote method
    @Test
    fun `verify handleMapNote with non-null note when non-null view is attached`() {
        every { view.displayNoteOnMap(any()) } answers { nothing }

        presenter.onAttach(view)
        presenter.handleMapNote(testNote)

        verify { view.displayNoteOnMap(testNote) }
    }

    @Test
    fun `verify handleMapNote with null note when non-null view is attached`() {
        presenter.onAttach(view)
        presenter.handleMapNote(null)

        verify(exactly = 0) { view.displayNoteOnMap(any()) }
    }

    @Test
    fun `verify handleMapNote with non-null note when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleMapNote(testNote)

        verify(exactly = 0) { view.displayNoteOnMap(any()) }
    }

    @Test
    fun `verify handleMapNote with null note when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleMapNote(null)

        verify(exactly = 0) { view.displayNoteOnMap(any()) }
    }

    @Test
    fun `verify handleMapNote with non-null note when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleMapNote(testNote)

        verify(exactly = 0) { view.displayNoteOnMap(any()) }
    }

    @Test
    fun `verify handleMapNote with null note when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleMapNote(null)

        verify(exactly = 0) { view.displayNoteOnMap(any()) }
    }

    // verify a handleLocationUpdate method
    @Test
    fun `verify handleLocationUpdate with interaction mode and new location when non-null view is attached`() {
        presenter.onAttach(view)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleLocationUpdate with interaction mode and the same location when non-null view is attached`() {
        presenter.onAttach(view)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleLocationUpdate with non-interaction mode and new location when non-null view is attached`() {
        every { view.animateCamera(any()) } answers { nothing }

        presenter.onAttach(view)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)

        verify { view.animateCamera(testLocation) }
    }

    @Test
    fun `verify handleLocationUpdate with non-interaction mode and the same location when non-null view is attached`() {
        every { view.animateCamera(any()) } answers { nothing }

        presenter.onAttach(view)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)

        verify { view.animateCamera(testLocation) }
    }

    @Test
    fun `verify handleLocationUpdate with interaction mode and new location when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleLocationUpdate with interaction mode and the same location when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleLocationUpdate with non-interaction mode and new location when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(testLocation) }
    }

    @Test
    fun `verify handleLocationUpdate with non-interaction mode and the same location when null view is attached`() {
        presenter.onAttach(null)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(testLocation) }
    }

    @Test
    fun `verify handleLocationUpdate with interaction mode and new location when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleLocationUpdate(isInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleLocationUpdate with interaction mode and the same location when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleLocationUpdate(isInteractionMode, testLocation)
        presenter.handleLocationUpdate(isInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(any()) }
    }

    @Test
    fun `verify handleLocationUpdate with non-interaction mode and new location when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(testLocation) }
    }

    @Test
    fun `verify handleLocationUpdate with non-interaction mode and the same location when view is detached`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)
        presenter.handleLocationUpdate(isNonInteractionMode, testLocation)

        verify(exactly = 0) { view.animateCamera(testLocation) }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}