package com.paraspatil.readstack.ui.library


import app.cash.turbine.test
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.domain.repository.BookRepository
import com.paraspatil.readstack.domain.util.NetworkResult
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.collections.emptyList

@OptIn(ExperimentalCoroutinesApi::class)
class LibraryViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: BookRepository
    private lateinit var viewModel: LibraryViewModel

    @Before
    fun setUp(){
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        every { repository.getLibrary() } returns flowOf(emptyList())
        every {repository.getSearchResults(any()) }returns flowOf(emptyList())
        every {repository.isOnline()} returns flowOf(true)

        viewModel = LibraryViewModel(repository)

    }
    @After
    fun tearDown(){
        Dispatchers.resetMain()
    }
    @Test
    fun uiState_emits_empty_library_initially()= runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertTrue(state.data?.isEmpty()?:true)
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun uiState_emits_books_from_library()=runTest {
        val fakeEntity = BookEntity(
            id = "1",
            title = "The Lord of the Rings",
            author = "J.R.R. Tolkien",
            thumbnailUrl = "https://picsum.photos/200/300",
            description = "Epic high-fantasy novel",
            pageCount = 1200,
            publishedDate = "1954"
        )
        every { repository.getLibrary() } returns flowOf(listOf(fakeEntity))
        every { repository.isOnline() } returns flowOf(true)


        val vm = LibraryViewModel(repository)

        vm.uiState.test {
            val state = awaitItem()
            assertEquals("The Lord of the Rings",state.data?.first()?.title)
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun uiState_reflects_offline_state()=runTest {
        every { repository.isOnline() } returns flowOf(false)

        val vm = LibraryViewModel(repository)

        vm.uiState.test {
            val state = awaitItem()
            assertTrue(state.isOffline)
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun onSearchQueryChange_updates_searchQuery()=runTest {
        viewModel.searchQuery.test {
            assertEquals("",awaitItem())
            viewModel.onSearchQueryChange("harry potter")
            val query = awaitItem()
            assertEquals("harry potter",query)
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun short_search_query_does_not_trigger_search()= runTest {
        coEvery {
            repository.searchBooksWithPagination(any(), any(), any(), any())
        }returns NetworkResult.Success(Unit)

        viewModel.onSearchQueryChange("h")
        viewModel.searchResults.test {
            val results = awaitItem()
            assertTrue(results.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}