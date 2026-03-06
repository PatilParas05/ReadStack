package com.paraspatil.readstack.navigation


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.ui.details.BookDetailScreen
import com.paraspatil.readstack.ui.library.LibraryScreen
import com.paraspatil.readstack.ui.library.LibraryViewModel
import com.paraspatil.readstack.ui.library.components.BrowserUtils

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context = LocalContext.current
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()
    NavHost(
        navController = navController,
        startDestination = AppScreens.LibraryScreen
    ) {
        composable<AppScreens.LibraryScreen> {
            val libraryViewModel: LibraryViewModel = hiltViewModel()
            LibraryScreen(
                viewModel = libraryViewModel,
                onBookClick = { book: Book ->
                    book.previewLink?.let { url ->
                        BrowserUtils.launchBrowser(
                            context = context,
                            url = url,
                            toolbarColor = surfaceColor
                        )
                    }
                },
                onInfoClick = { bookId ->
                    navController.navigate(AppScreens.BookDetailScreen(bookId = bookId))
                }
            )
        }

        composable<AppScreens.BookDetailScreen> { backStackEntry ->
            val args = backStackEntry.toRoute<AppScreens.BookDetailScreen>()
            BookDetailScreen(
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}