package com.paraspatil.readstack.ui.library.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun OfflineBanner(isOffline: Boolean){
    var showConnectedMessage by remember { mutableStateOf(false) }
    var isFirstLoad by remember { mutableStateOf(true) }
    LaunchedEffect(isOffline) {
        if (isFirstLoad){
            isFirstLoad = false
            return@LaunchedEffect
        }
        if (!isOffline) {
            showConnectedMessage = true
            delay(5000)
            showConnectedMessage = false
        }
    }
    AnimatedVisibility(
        visible = isOffline || showConnectedMessage,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        val isBackOnline = !isOffline
        val backgroundColor = if (isBackOnline) {
            androidx.compose.ui.graphics.Color(0xFF4CAF50)
        }else{
            MaterialTheme.colorScheme.errorContainer
        }

        val contentColor = if (isBackOnline) {
            androidx.compose.ui.graphics.Color.White
        }else{
            MaterialTheme.colorScheme.onErrorContainer
        }

        Surface(
            color = backgroundColor,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Icon(
                    imageVector = if (isBackOnline) Icons.Default.Wifi else Icons.Default.CloudOff,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = contentColor
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = if (isBackOnline)"Back Online" else "No Internet Connection",
                    style = MaterialTheme.typography.labelMedium,
                    color = contentColor
                )
            }
        }
    }
}