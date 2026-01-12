package com.paraspatil.readstack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.paraspatil.readstack.ui.library.LibraryScreen
import com.paraspatil.readstack.ui.library.LibraryViewModel
import com.paraspatil.readstack.ui.theme.ReadStackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReadStackTheme {
                val viewModel: LibraryViewModel = hiltViewModel()
                LibraryScreen(viewModel = viewModel)
            }
        }
    }
}
