package com.paraspatil.readstack.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraspatil.readstack.domain.model.toDomain
import com.paraspatil.readstack.domain.repository.BookRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

    val books = repository.getLibrary()
        .map { list -> list.map { it.toDomain() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun searchBooks(query: String) {
        viewModelScope.launch {
            repository.searchBooks(query)
        }
    }
}
