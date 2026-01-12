package com.paraspatil.readstack.ui.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity
import com.paraspatil.readstack.data.remote.toBookEntity
import com.paraspatil.readstack.domain.repository.BookRepository
import com.paraspatil.readstack.domain.util.NetworkResult
import com.paraspatil.readstack.domain.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val repository: BookRepository
) : ViewModel() {

 private val _searchQuery= MutableStateFlow("")
    val searchQuery=_searchQuery.asStateFlow()

    private val _isSearching= MutableStateFlow(false)
    val isSearching=_isSearching.asStateFlow()

    private val _searchResults= MutableStateFlow<List<SearchResultEntity>>(emptyList())
    val searchResults=_searchResults.asStateFlow()

    private val _searchError= MutableStateFlow<String?>(null)
    val searchError=_searchError.asStateFlow()

    val library: StateFlow<List<BookEntity>> = repository.getLibrary()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _isRefreshing= MutableStateFlow(false)
    val isRefreshing=_isRefreshing.asStateFlow()

    val isOnline:StateFlow<Boolean> = repository.isOnline()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    val uiState: StateFlow<UiState<List<BookEntity>>> = combine(
        library,
        _isRefreshing,
        _searchError,
        isOnline

    ){
        books,loading,error,online->
        UiState(
            data=books,
            isLoading = loading,
            error=error,
            isOffline = !online
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UiState()
    )

    fun onSearchQueryChange(newQuery:String){
        _searchQuery.value=newQuery

        if (newQuery.isBlank()){
            _searchResults.value=emptyList()
            _searchError.value=null
        return
        }

        viewModelScope.launch {
            repository.getSearchResults(newQuery)
            .collect{
                cacheResult->
                _searchResults.value=cacheResult

            }

        }
    }

    fun searchBooks(){
        val query=searchQuery.value.trim()
        if (query.isBlank()) return

        viewModelScope.launch {
            _isSearching.value=true
            _searchError.value=null

            when (val result=repository.searchBooks(query)){
                is NetworkResult.Success->{
                    _searchError.value = null
                }
                is NetworkResult.Error->{
                    _searchError.value=result.message
                }
                is NetworkResult.Offline->{
                    _searchError.value="No internet connection. Showing cached results"
                }
                is NetworkResult.Loading->{}
            }
            _isSearching.value=false

        }
    }
    fun clearSearch(){
        _searchQuery.value=""
        _searchResults.value= emptyList()
        _searchError.value=null
    }
    fun addLibrary(searchResultEntity: SearchResultEntity){
        viewModelScope.launch {
            val bookEntity=searchResultEntity.toBookEntity()
            repository.addBookToLibrary(bookEntity)
        }

    }
    fun deleteBook(book: BookEntity){
        viewModelScope.launch {
            repository.deleteBook(book)
        }
    }
    fun refreshLibrary(){
        viewModelScope.launch {
            _isRefreshing.value=true
            repository.syncLibrary()
            _isRefreshing.value=false
        }

    }

    fun clearLibrary(){
        viewModelScope.launch {
            repository.clearLibrary()
        }
    }






}
