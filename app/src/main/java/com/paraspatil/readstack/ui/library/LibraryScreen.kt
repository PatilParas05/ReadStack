package com.paraspatil.readstack.ui.library


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
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
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.ui.library.components.BookCard
import com.paraspatil.readstack.ui.library.components.OfflineBanner
import com.paraspatil.readstack.ui.library.components.QuoteShareCard
import com.paraspatil.readstack.ui.library.components.shareQuote
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    viewModel: LibraryViewModel,
    onInfoClick: (String) -> Unit,
    onBookClick: (Book) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()


    var selectedTab by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val coroutineScope = rememberCoroutineScope()

    var showQuoteDialog by remember { mutableStateOf(false) }
    var selectedBookForQuote by remember { mutableStateOf<Book?>(null) }
    var quoteText by remember { mutableStateOf("") }
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    val context = LocalContext.current

    if (showQuoteDialog && selectedBookForQuote != null){
        androidx.compose.ui.window.Dialog(onDismissRequest = {showQuoteDialog = false}) {
            Card{
                Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.drawWithContent{
                        graphicsLayer.record{
                            this@drawWithContent.drawContent()
                        }
                        drawLayer(graphicsLayer)
                    }){
                        QuoteShareCard(book = selectedBookForQuote!!, quote = quoteText)
                    }
                    OutlinedTextField(
                        value = quoteText,
                        onValueChange = { quoteText = it },
                        label = {Text("Enter your Favorite Quote")}
                        )
                    Button(
                        modifier = Modifier.padding(top = 16.dp),
                        onClick = {
                            coroutineScope.launch {
                                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                                shareQuote(context,bitmap)
                                showQuoteDialog = false
                            }
                        }
                    ) {
                        Text("Share Image")
                    }
                }
              }
        }
    }

    LaunchedEffect(uiState.data?.size) {
       if (uiState.data?.size==1){
           snackbarHostState.showSnackbar(
               message = "Tip: Swipe left on book to delete it",
               withDismissAction = true
           )
       }
    }
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
                    var menuExpanded by remember { mutableStateOf(false) }
                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More Options")
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false })
                    {
                        DropdownMenuItem(text = { Text("Refresh Library") }, onClick = { viewModel.refreshLibrary(); menuExpanded = false })
                        DropdownMenuItem(text = { Text("Clear Library") }, onClick = { viewModel.clearLibrary(); menuExpanded = false })

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
            OfflineBanner(isOffline = uiState.isOffline)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                label = { Text("Search Books") },
                trailingIcon = {
                    Row {
                        AnimatedVisibility(visible = searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.clearSearch() }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                            }
                        }
                        AnimatedVisibility(visible = !isSearching) {
                            IconButton(onClick = {
                                selectedTab = 1
                                keyboardController?.hide()
                            }) {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                            }
                        }

                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        selectedTab = 1
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
                        onBookClick = onBookClick,
                        onInfoClick = onInfoClick,
                        onQuoteClick = { book ->
                            selectedBookForQuote = book
                            showQuoteDialog = true
                        }
                    )
                }
                1 -> {
                    SearchTab(
                        searchQuery = searchQuery,
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
                        onBookClick = onBookClick

                    )
                }
            }
        }
    }
}

@Composable
fun LibraryTab(
    books: List<Book>,
    onDeleteBook: (Book) -> Unit,
    onBookClick: (Book) -> Unit,
    onInfoClick: (String) -> Unit,
    onQuoteClick: (Book) -> Unit
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
            items(books, key = { it.id }) { book ->
                val dismissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = { value ->
                        when (value) {
                            SwipeToDismissBoxValue.EndToStart -> {
                                onDeleteBook(book)
                                true
                            }
                            SwipeToDismissBoxValue.StartToEnd -> {
                                false
                            }
                            else -> false
                        }
                    }
                )
                SwipeToDismissBox(
                    state = dismissState,
                    enableDismissFromStartToEnd = false,
                    backgroundContent = {
                        val color = when (dismissState.dismissDirection) {
                            SwipeToDismissBoxValue.EndToStart -> Color(0xFFE57373)
                            else -> Color.Transparent
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color, MaterialTheme.shapes.medium)
                                .padding(horizontal = 20.dp),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            if (dismissState.progress > 0.1f && dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Release to Delete",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onErrorContainer,
                                        modifier = Modifier.padding(end = 12.dp)
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete Book",
                                        tint = if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart)
                                            MaterialTheme.colorScheme.error
                                        else Color.Transparent
                                    )
                                }
                            }
                        }
                    }
                ) {
                    BookCard(
                        book = book,
                        title = book.title,
                        author = book.author,
                        thumbnailUrl = book.thumbnailUrl ?: "",
                        description = book.description,
                        onActionClick = { },
                        actionIcon = Icons.Default.Delete,
                        actionContentDescription = "",
                        onCardClick = { onBookClick(book) },
                        onInfoClick = { onInfoClick(book.id) },
                        onQuoteClick = {
                            onQuoteClick(book)
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun SearchTab(
    searchQuery: String,
    searchResults: List<Book>,
    isSearching: Boolean,
    onAddToLibrary: (Book) -> Unit,
    onLoadMore: () -> Unit,
    onBookClick: (Book) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty())listState.scrollToItem(0)
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
                        book = result,
                        title = result.title,
                        author = result.author,
                        thumbnailUrl = result.thumbnailUrl ?: "",
                        description = result.description,
                        onActionClick = { onAddToLibrary(result) },
                        actionIcon = Icons.Default.Add,
                        actionContentDescription = "Add to library",
                        onCardClick = { onBookClick(result) },
                        onQuoteClick = {},
                        onInfoClick = null
                    )
                }

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


