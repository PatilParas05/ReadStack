package com.paraspatil.readstack.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.domain.model.toDomain
import com.paraspatil.readstack.domain.model.toEntity
import com.paraspatil.readstack.domain.repository.BookRepository
import com.paraspatil.readstack.domain.util.NetworkResult
import com.paraspatil.readstack.domain.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.emptyList

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {
    private val _maxResults = 20
    private var searchJob: Job? = null
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchError = MutableStateFlow<String?>(null)


    private val _currentPage = MutableStateFlow(0)

    val searchResults: StateFlow<List<Book>> = _searchQuery
        .debounce(500L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            val trimmedQuery = query.trim()
            if (trimmedQuery.length < 2){
                _isSearching.value = false
                flowOf(emptyList())
            }else{
                executeSearch(trimmedQuery,isNewSearch = true)
                    repository.getSearchResults(trimmedQuery)
                    .map{ list -> list.map { it.toDomain() }}
                }
            }
            .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = emptyList()
                )

    private val _isRefreshing = MutableStateFlow(false)

    val isOnline: StateFlow<Boolean> = repository.isOnline()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = true
        )
    val uiState: StateFlow<UiState<List<Book>>> = combine(
        repository.getLibrary().map { it.map { it.toDomain() } },
        _isRefreshing,
        _searchError,
        repository.isOnline()
    ) { books, loading, error, online ->
        UiState(
            data = books,
            isLoading = loading,
            error = error,
            isOffline = !online
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    private fun executeSearch(query:String,isNewSearch: Boolean) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _isSearching.value = true
            _searchError.value = null

            if (isNewSearch) {
                _currentPage.value = 0
            } else {
                _currentPage.value++
            }

            val startIndex = _currentPage.value * _maxResults

            when (val result = repository.searchBooksWithPagination(
                query = query,
                startIndex = startIndex,
                maxResults = _maxResults,
                shouldReplace = isNewSearch
            )) {
                is NetworkResult.Success -> {
                    _searchError.value = null
                }
                is NetworkResult.Error -> {
                    _searchError.value = result.message
                    if (!isNewSearch) _currentPage.value--
                }
                is NetworkResult.Offline -> {
                    _searchError.value = "No internet connection. Showing cached results"
                    if (!isNewSearch) _currentPage.value--
                }
                is NetworkResult.Loading -> {}
            }
            _isSearching.value = false
        }
    }

    fun loadMore() {
        val query = _searchQuery.value.trim()
        if (!isSearching.value && query.length >= 2) {
            executeSearch(query,isNewSearch = false)
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchError.value = null
        _currentPage.value = 0
        _isSearching.value = false
        searchJob?.cancel()
        viewModelScope.launch {
            repository.clearSearchCache("")
        }
    }

    fun addLibrary(book: Book) {
        viewModelScope.launch {
            repository.addBookToLibrary(book.toEntity())
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.deleteBook(book.toEntity())
        }
    }

    fun refreshLibrary() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.syncLibrary()
            _isRefreshing.value = false
        }
    }

    fun clearLibrary() {
        viewModelScope.launch {
            repository.clearLibrary()
        }
    }
}
