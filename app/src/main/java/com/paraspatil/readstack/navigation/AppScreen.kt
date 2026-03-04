package com.paraspatil.readstack.navigation

import kotlinx.serialization.Serializable
@Serializable
sealed interface AppScreens{
@Serializable
    data object LibraryScreen : AppScreens
@Serializable
data class BookDetailScreen (val bookId: String) : AppScreens
}
