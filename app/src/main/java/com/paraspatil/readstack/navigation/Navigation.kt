package com.paraspatil.readstack.navigation

sealed class AppScreens(val route: String) {
    object LibraryScreen : AppScreens("library_screen")
    object BookDetailScreen : AppScreens("book_detail_screen")
}
