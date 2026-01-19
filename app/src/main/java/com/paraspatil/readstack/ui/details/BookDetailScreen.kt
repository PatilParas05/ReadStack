package com.paraspatil.readstack.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.paraspatil.readstack.domain.model.Book

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailScreen(
    viewModel: BookDetailViewModel= hiltViewModel(),
    onNavigateUp: () -> Unit

){
    val book by viewModel.book.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Book Details") },
                navigationIcon ={
                    IconButton(onClick = onNavigateUp){
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            if (book == null) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                BookDetailContent(book!!)
            }
        }
    }
}

@Composable
fun BookDetailContent(book: Book) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = book.thumbnailUrl,
            contentDescription = "Book cover",
            modifier = Modifier.height(300.dp).fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium,
            text = book.title
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            text = book.author
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Pages: ${book.pageCount}",
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = book.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
    }
}

@Preview
@Composable
fun BookDetailScreenPreview(){
    BookDetailContent(
        Book(
            id= "1",
            title= "The Lord of the Rings",
            author= "J.R.R. Tolkien",
            thumbnailUrl= "https://picsum.photos/200/300",
            description= "A long description of the book",
            pageCount= 1200
        )
    )
}
