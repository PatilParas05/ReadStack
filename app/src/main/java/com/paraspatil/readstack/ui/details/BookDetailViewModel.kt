package com.paraspatil.readstack.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.domain.model.toDomain
import com.paraspatil.readstack.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
@HiltViewModel
class BookDetailViewModel @Inject constructor(
    private val repository: BookRepository,
    savedStateHandle: SavedStateHandle
): ViewModel(){
    private val bookId: String = savedStateHandle.get<String>("bookId")!!

    val book: StateFlow<Book?> = repository.getBookById(bookId)
        .map { it?.toDomain() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

}
