package com.paraspatil.readstack.ui.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.domain.repository.BookRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
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

@OptIn(ExperimentalCoroutinesApi::class)
class BookDetailViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: BookRepository
    private lateinit var viewModel: BookDetailViewModel
    private lateinit var savedStateHandle: SavedStateHandle

    private val fakeBookEntity = BookEntity(
        id = "1",
        title = "The Lord of the Rings",
        author = "J.R.R. Tolkien",
        thumbnailUrl = "https://picsum.photos/200/300",
        description = "Epic high-fantasy novel",
        pageCount = 1200,
        publishedDate = "1954"
    )
    @Before
    fun setUp() {
    Dispatchers.setMain(testDispatcher)
        repository = mockk()
        savedStateHandle = SavedStateHandle(mapOf("bookId" to "1"))
        every { repository.getBookById("1") } returns flowOf(fakeBookEntity)
        viewModel = BookDetailViewModel(repository, savedStateHandle)

    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }
    @Test
    fun book_StateFlow_emits_mapped_domain_Book()= runTest {
        viewModel.book.test {
            val book = awaitItem()
            assertNotNull(book)
            assertEquals("The Lord of the Rings",book?.title)
            assertEquals("J.R.R. Tolkien",book?.author)
            assertEquals(1200,book?.pageCount)
            cancelAndIgnoreRemainingEvents()
        }
    }
    @Test
    fun book_StateFlow_emits_null_when_book_not_found()= runTest {
        every { repository.getBookById("1") } returns flowOf(null)
        val vm = BookDetailViewModel(repository, savedStateHandle)
        vm.book.test {
            assertNull(awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}

