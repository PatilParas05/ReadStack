package com.paraspatil.readstack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.paraspatil.readstack.navigation.AppNavigation
import com.paraspatil.readstack.ui.theme.ReadStackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ReadStackTheme {
                AppNavigation()
            }
        }
    }
}
