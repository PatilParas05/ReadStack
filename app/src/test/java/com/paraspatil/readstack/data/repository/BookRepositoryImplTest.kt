package com.paraspatil.readstack.data.repository

import com.paraspatil.readstack.data.local.BookDao
import com.paraspatil.readstack.data.remote.BookItemDto
import com.paraspatil.readstack.data.remote.BookResponseDto
import com.paraspatil.readstack.data.remote.GoogleBookApi
import com.paraspatil.readstack.data.remote.VolumeInfoDto
import com.paraspatil.readstack.data.repository.BookRepositoryImpl
import com.paraspatil.readstack.data.util.NetworkMonitor
import com.paraspatil.readstack.domain.util.NetworkResult
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.collections.listOf

class BookRepositoryImplTest {
    private lateinit var api : GoogleBookApi
    private lateinit var repository: BookRepositoryImpl
    private lateinit var dao: BookDao
    private lateinit var networkMonitor: NetworkMonitor

    @Before
    fun setUp() {
        api = mockk()
        dao = mockk(relaxed = true)
        networkMonitor = mockk()
        repository = BookRepositoryImpl(api, dao, networkMonitor, mockk(relaxed = true))

    }
    @Test
    fun searchBooks_return_offline_when_no_internet()= runTest {
        every { networkMonitor.isCurrentlyOnline() } returns false
        val result = repository.searchBooks("harry potter")
        assert(result is NetworkResult.Offline)
    }
    @Test
    fun searchBooks_return_success_and_save_results_to_DB()= runTest {
        every { networkMonitor.isCurrentlyOnline() } returns true
        val fakeResponse = BookResponseDto(
            items = listOf(
                BookItemDto(
                    id = "1",
                    volumeInfo = VolumeInfoDto(
                        title = "The Lord of the Rings",
                        authors = listOf("J.R.R. Tolkien")
                    )
                )
            )
        )
        coEvery { api.searchBooks("harry potter", any()) } returns fakeResponse
        coEvery { dao.clearSearchResults("harry potter") } just Runs
        coEvery { dao.upsertSearchResult(any()) } just Runs

        val result = repository.searchBooks("harry potter")
        assert(result is NetworkResult.Success)
        coVerify { dao.upsertSearchResult(any()) }
    }

@Test
fun searchBooksW_return_error_when_api_throws_exception()= runTest {
    every { networkMonitor.isCurrentlyOnline() } returns true
    coEvery { api.searchBooks(any(), any()) } throws RuntimeException("server down")

    val result = repository.searchBooks("harry potter")
    assert(result is NetworkResult.Error)
    assertTrue(result is NetworkResult.Error)
    assertEquals("server down", (result as NetworkResult.Error).message)
    }
}

