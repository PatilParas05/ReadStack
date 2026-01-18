package com.paraspatil.readstack.navigation

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.paraspatil.readstack.domain.model.Book
import com.paraspatil.readstack.ui.details.BookDetailScreen
import com.paraspatil.readstack.ui.library.LibraryScreen
import com.paraspatil.readstack.ui.library.LibraryViewModel

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    val context=LocalContext.current

    NavHost(
        navController = navController,
        startDestination = AppScreens.LibraryScreen.route
    ) {
        composable(AppScreens.LibraryScreen.route) {
            val libraryViewModel: LibraryViewModel = hiltViewModel()
            LibraryScreen(
                viewModel = libraryViewModel,
                onBookClick = { book: Book ->
                    book.previewLink?.let {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                        context.startActivity(intent)
                    }
                },
                onInfoClick = { bookId ->
                    navController.navigate("${AppScreens.BookDetailScreen.route}/$bookId")
                }
            )
        }
        composable(
            route = "${AppScreens.BookDetailScreen.route}/{bookId}",
            arguments = listOf(navArgument("bookId") { })
        ) {
            BookDetailScreen(onNavigateUp = { navController.navigateUp() })
        }
    }
}
