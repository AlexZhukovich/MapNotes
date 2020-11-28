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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.loadKoinModules
import org.koin.core.context.stopKoin
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SearchNotesPresenterTest {
    private val view: SearchNotesView = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val notesRepository: NotesRepository = mockk(relaxed = true)
    private val appExecutors: AppExecutors = mockk(relaxed = true)

    private val presenter by lazy { SearchNotesPresenter(appExecutors, userRepository, notesRepository) }

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
        every { appExecutors.uiContext } returns Dispatchers.Unconfined
        every { appExecutors.networkContext } returns Dispatchers.Unconfined
    }

    @Test
    fun `verify getNotes when non-null view is attached and notesRepository returns Error`() = runBlocking {
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

        coEvery { notesRepository.getNotes(any()) } returns Result.Success(testNotes)

        presenter.onAttach(view)
        presenter.getNotes(unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = expectedListSize) { view.displayNote(any()) }
    }

    @Test
    fun `verify getNotes when non-null view is attached and notes repository returns empty correct data`() {
        coEvery { notesRepository.getNotes(any()) } returns Result.Success(listOf())

        presenter.onAttach(view)
        presenter.getNotes(unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = 0) { view.displayNote(any()) }
    }

    @Test
    fun `verify getNotes when null view is attached and notes repository returns correct data`() {
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
        coEvery { notesRepository.getNotesByNoteText(searchByNoteRequest, any()) } returns Result.Success(testNotes)

        presenter.onAttach(view)
        presenter.searchNotes(searchByNoteRequest, noteCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = expectedListSize) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by note when non-null view is attached and notesRepository returns empty correct data`() {
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

    @Test
    fun `verify searchNotes by note when non-null view is attached, text is empty and notesRepository returns correct data`() {
        val emptySearchRequest = ""
        every { view.displayNote(any()) } answers { nothing }
        coEvery { notesRepository.getNotes(any()) } returns Result.Success(listOf())

        presenter.onAttach(view)
        presenter.searchNotes(emptySearchRequest, noteCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = 0) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by user when non-null view is attached, text is empty and notesRepository returns correct data`() {
        val emptySearchRequest = ""

        coEvery { notesRepository.getNotes(any()) } returns Result.Success(listOf())

        presenter.onAttach(view)
        presenter.searchNotes(emptySearchRequest, userCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify(exactly = 0) { view.displayNote(any()) }
    }

    @Test
    fun `verify searchNotes by user when non-null view is attached, userRepository returns Error`() {
        coEvery { userRepository.getUserIdFromHumanReadableName(any()) } returns Result.Error(RuntimeException())

        presenter.onAttach(view)
        presenter.searchNotes(searchByUserUIDRequest, userCategoryInSearch, unknownUserName)

        verify { view.clearSearchResults() }
        verify { view.displayUnknownUserError() }
    }

    @After
    fun tearDown() {
        stopKoin()
    }
}