package com.alex.mapnotes.search

import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.data.Result
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.di.appModule
import com.alex.mapnotes.model.Note
import io.mockk.mockk
import io.mockk.every
import io.mockk.coEvery
import io.mockk.verify
import io.mockk.coVerify
import kotlinx.coroutines.experimental.android.UI
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.standalone.StandAloneContext.closeKoin
import org.koin.standalone.StandAloneContext.loadKoinModules
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchNotesPresenterTest {
    private val view: SearchNotesView = mockk()
    private val userRepository: UserRepository = mockk()
    private val notesRepository: NotesRepository = mockk()
    private val appExecutors: AppExecutors = mockk()

    private val presenter by lazy { SearchNotesPresenter(userRepository, notesRepository, appExecutors) }

    private val noteCategoryInSearch = 0
    private val userCategoryInSearch = 1
    private val unknownUserName = "Unknown"
    private val searchByNoteRequest = "test note"
    private val searchByUserUIDRequest = "22222222"
    private val testNotes: List<Note> = listOf(
            Note(text = "test note 1_1", user = "11111111"),
            Note(text = "test note 2_1", user = "11111111"),
            Note(text = "test note 1_2", user = "22222222"),
            Note(text = "test note 2_2", user = "22222222"),
            Note(text = "test note 3_2", user = "22222222"))

    @Before
    fun setUp() {
        loadKoinModules(listOf(appModule))
        every { appExecutors.uiContext } returns UI
        every { appExecutors.networkContext } returns UI
        every { view.clearSearchResults() } answers { nothing }
        every { view.displayLoadingNotesError() } answers { nothing }
    }

    @Test
    fun `verify getNotes when non-null view is attached and notesRepository returns Error`() {
        coEvery { notesRepository.getNotes(any()) } returns Result.Error(RuntimeException())

        presenter.onAttach(view)
        presenter.getNotes(unknownUserName)

        verify { view.clearSearchResults() }
        verify { view.displayLoadingNotesError() }
    }

    @Test
    fun `verify getNotes when null view is attached and notesRepository returns Error`() {
        presenter.onAttach(null)
        presenter.getNotes(unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotes(any()) }
    }

    @Test
    fun `verify getNotes when view is detached and notesRepository returns Error`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.getNotes(unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotes(any()) }
    }

    @Test
    fun `verify getNotes when non-null view is attached and notes repository returns non-empty correct data`() {
        val expectedListSize = testNotes.size

        every { view.displayNote(any()) } answers { nothing }
        coEvery { notesRepository.getNotes(any()) } returns Result.Success(testNotes)

        presenter.onAttach(view)
        presenter.getNotes(unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = expectedListSize) { view.displayNote(any()) }
    }

    @Test
    fun `verify getNotes when non-null view is attached and notes repository returns empty correct data`() {
        every { view.displayNote(any()) } answers { nothing }
        coEvery { notesRepository.getNotes(any()) } returns Result.Success(listOf())

        presenter.onAttach(view)
        presenter.getNotes(unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = 0) { view.displayNote(any()) }
    }

    @Test
    fun `verify getNotes when null view is attached and notes repository returns correct data`() {
        presenter.onAttach(null)
        presenter.getNotes(unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotes(any()) }
    }

    @Test
    fun `verify getNotes when view is detached and notes repository returns correct data`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.getNotes(unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotes(any()) }
    }

    @Test
    fun `verify searchNotes by note when non-null view is attached and notesRepository returns Error`() {
        coEvery { notesRepository.getNotesByNoteText(searchByNoteRequest, any()) } returns Result.Error(RuntimeException())

        presenter.onAttach(view)
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify { view.displayLoadingNotesError() }
    }

    @Test
    fun `verify searchNotes by note when null view is attached and notesRepository returns Error`() {
        presenter.onAttach(null)
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotesByNoteText(any(), any()) }
    }

    @Test
    fun `verify searchNotes by note when view is detached and notesRepository returns Error`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotesByNoteText(any(), any()) }
    }

    @Test
    fun `verify searchNotes by note when non-null view is attached and notesRepository returns non-empty correct data`() {
        val expectedListSize = testNotes.size
        every { view.displayNote(any()) } answers { nothing }
        coEvery { notesRepository.getNotesByNoteText(searchByNoteRequest, any()) } returns Result.Success(testNotes)

        presenter.onAttach(view)
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = expectedListSize) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by note when non-null view is attached and notesRepository returns empty correct data`() {
        every { view.displayNote(any()) } answers { nothing }
        coEvery { notesRepository.getNotesByNoteText(searchByNoteRequest, any()) } returns Result.Success(listOf())

        presenter.onAttach(view)
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = 0) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by note when null view is attached and notesRepository returns correct data`() {
        presenter.onAttach(null)
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotesByNoteText(any(), any()) }
    }

    @Test
    fun `verify searchNotes by note when view is detached and notesRepository returns correct data`() {
        presenter.onAttach(view)
        presenter.onDetach()

        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { notesRepository.getNotesByNoteText(any(), any()) }
    }

    @Test
    fun `verify searchNotes by user when non-null view is attached and notesRepository returns Error`() {
        coEvery { userRepository.getUserIdFromHumanReadableName(searchByUserUIDRequest) } answers { Result.Success(searchByUserUIDRequest) }
        coEvery { notesRepository.getNotesByUser(searchByUserUIDRequest, any()) } answers { Result.Error(RuntimeException()) }

        presenter.onAttach(view)
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify { view.displayLoadingNotesError() }
    }

    @Test
    fun `verify searchNotes by user when null view is attached and notesRepository returns Error`() {
        presenter.onAttach(null)
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { userRepository.getUserIdFromHumanReadableName(any()) }
        coVerify(exactly = 0) { notesRepository.getNotesByUser(any(), any()) }
    }

    @Test
    fun `verify searchNotes by user when view is detached and notesRepository returns Error`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { userRepository.getUserIdFromHumanReadableName(any()) }
        coVerify(exactly = 0) { notesRepository.getNotesByUser(any(), any()) }
    }

    @Test
    fun `verify searchNotes by user when non-null view is attached and notesRepository returns non-empty correct data and`() {
        val expectedListSize = testNotes.size
        every { view.displayNote(any()) } answers { nothing }
        coEvery { userRepository.getUserIdFromHumanReadableName(searchByUserUIDRequest) } answers { Result.Success(searchByUserUIDRequest) }
        coEvery { notesRepository.getNotesByUser(searchByUserUIDRequest, any()) } answers { Result.Success(testNotes) }

        presenter.onAttach(view)
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = expectedListSize) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by user when non-null view is attached and notesRepository returns empty correct data and`() {
        coEvery { userRepository.getUserIdFromHumanReadableName(searchByUserUIDRequest) } answers { Result.Success(searchByUserUIDRequest) }
        coEvery { notesRepository.getNotesByUser(searchByUserUIDRequest, any()) } answers { Result.Success(listOf()) }

        presenter.onAttach(view)
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = 0) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by user when null view is attached and notesRepository returns correct data`() {
        presenter.onAttach(null)
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { userRepository.getUserIdFromHumanReadableName(any()) }
        coVerify(exactly = 0) { notesRepository.getNotesByUser(any(), any()) }
    }

    @Test
    fun `verify searchNotes by user when view is detached and notesRepository returns correct data`() {
        presenter.onAttach(view)
        presenter.onDetach()
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        coVerify(exactly = 0) { userRepository.getUserIdFromHumanReadableName(any()) }
        coVerify(exactly = 0) { notesRepository.getNotesByUser(any(), any()) }
    }

    @After
    fun tearDown() {
        closeKoin()
    }
}