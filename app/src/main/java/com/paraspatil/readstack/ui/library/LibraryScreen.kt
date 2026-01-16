package com.paraspatil.readstack.ui.library


import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.paraspatil.readstack.data.local.BookEntity
import com.paraspatil.readstack.data.local.SearchResultEntity
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(viewModel: LibraryViewModel) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    val currentSearchQuery by viewModel.currentSearchQuery.collectAsState()

    var selectedTab by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ReadStack") },
                actions = {
                    if (!isOnline) {
                        Icon(
                            Icons.Default.CloudOff,
                            contentDescription = "Offline",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false })
                    {
                        DropdownMenuItem(text = { Text("Refresh Library") }, onClick = { viewModel.refreshLibrary(); menuExpanded=false })
                        DropdownMenuItem(text = { Text("Clear Library") }, onClick = { viewModel.clearLibrary(); menuExpanded=false })

                    }
                }
            )

        },
        snackbarHost = { SnackbarHost(snackbarHostState) }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
        )
        {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Search Books") },
                trailingIcon = {
                    Row {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                            }
                        }
                        IconButton(onClick = {
                            selectedTab = 1
                            viewModel.searchBooks(isNewSearch = true)
                            keyboardController?.hide()
                        }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        selectedTab = 1
                        viewModel.searchBooks(isNewSearch = true)
                        keyboardController?.hide()
                    }
                ),
                singleLine = true
            )
            AnimatedVisibility(
                visible = isSearching || uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("My Library (${uiState.data?.size ?: 0})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Search (${searchResults.size})") }
                )
            }
            when (selectedTab) {
                0 -> {
                    LibraryTab(books = uiState.data ?: emptyList(), onDeleteBook = { viewModel.deleteBook(it) },
                        onBookClick= {book ->
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data=Uri.parse("https://books.google.com/book?id=${book.id}")
                           }
                            context.startActivity(intent)
                        }
                    )
                }
                1 -> {
                    SearchTab(
                        searchQuery = currentSearchQuery,
                        searchResults = searchResults,
                        isSearching = isSearching,
                        onAddToLibrary = { result ->
                            viewModel.addLibrary(result)
                            coroutineScope.launch {
                                snackbarHostState.currentSnackbarData?.dismiss()
                                snackbarHostState.showSnackbar("Book added to library")
                            }
                        },
                        onLoadMore = { viewModel.loadMore() },
                        onBookClick={result ->
                            val intent = Intent(Intent.ACTION_VIEW).apply {
                            data=Uri.parse("https://books.google.com/book?id=${result.id}")
                        }
                            context.startActivity(intent)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryTab(
    books: List<BookEntity>,
    onDeleteBook: (BookEntity) -> Unit,
    onBookClick: (BookEntity) -> Unit
) {
    if (books.isEmpty()) {
        Spacer(modifier = Modifier.padding(top = 30.dp))
        EmptyState(message = "Your  library is empty. \nSearch and add books!..")
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(books, key = { it.id }) {
                book ->
                BookCard(
                    title = book.title,
                    author = book.author,
                    thumbnailUrl = book.thumbnailUrl,
                    description = book.description,
                    onActionClick = { onDeleteBook(book) },
                    actionIcon = Icons.Default.Delete,
                    actionContentDescription = "Remove from library",
                    onCardClick = { onBookClick(book) }
                )
            }

        }
    }
}


@Composable
fun SearchTab(
    searchQuery: String,
    searchResults: List<SearchResultEntity>,
    isSearching: Boolean,
    onAddToLibrary: (SearchResultEntity) -> Unit,
    onLoadMore: () -> Unit,
    onBookClick: (SearchResultEntity) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        listState.scrollToItem(0)
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index == listState.layoutInfo.totalItemsCount - 1 && !isSearching
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            onLoadMore()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (searchResults.isEmpty() && !isSearching) {
            EmptyState(message = "Search for books to see results")
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(searchResults, key = { it.id }) { result ->
                    BookCard(
                        title = result.title,
                        author = result.author,
                        thumbnailUrl = result.thumbnailUrl,
                        description = result.description,
                        onActionClick = { onAddToLibrary(result) },
                        actionIcon = Icons.Default.Add,
                        actionContentDescription = "Add to library",
                        onCardClick = { onBookClick(result) }
                    )
                }
                if (isSearching) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }

    }
}


@Composable
fun BookCard(
    title: String,
    author: String,
    thumbnailUrl: String,
    description: String?,
    onActionClick: () -> Unit,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector,
    actionContentDescription: String,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically

        ) {
            AsyncImage(
                model = thumbnailUrl,
                contentDescription = null,
                modifier = Modifier.size(80.dp, 120.dp),
                contentScale = ContentScale.Crop

            )
            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis

                )
                Text(
                    text = author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (description != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            IconButton(onClick = onActionClick) {
                Icon(
                    imageVector = actionIcon,
                    contentDescription = actionContentDescription,
                    tint = MaterialTheme.colorScheme.primary

                )
            }
        }

    }
}


@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
