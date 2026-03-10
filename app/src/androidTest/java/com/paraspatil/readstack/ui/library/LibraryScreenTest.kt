package com.paraspatil.readstack.ui.library

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.domain.util.UiState
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class LibraryScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun libraryScreen_showBooks(){
        val fakeBook = Book(
            id = "1",
            title = "The Lord of the Rings",
            author = "J.R.R. Tolkien",
            thumbnailUrl = "",
            description = "Epic high-fantasy novel",
            pageCount = 1200,
            publishedDate = "1954"
        )
        val viewModel = mockk<LibraryViewModel>(relaxed = true)

        every { viewModel.uiState} returns MutableStateFlow(UiState(data = listOf(fakeBook)))
        every { viewModel.searchQuery } returns MutableStateFlow("")
        every { viewModel.isSearching } returns MutableStateFlow(false)
        every { viewModel.isOnline } returns MutableStateFlow(true)
        every { viewModel.searchResults } returns MutableStateFlow(emptyList<Book>())

        composeTestRule.setContent {
            LibraryScreen(viewModel = viewModel, onInfoClick = {},onBookClick = {})
        }
        composeTestRule.onNodeWithText("The Lord of the Rings").assertIsDisplayed()

    }
}
