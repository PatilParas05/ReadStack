package com.paraspatil.readstack.ui.library.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.paraspatil.readstack.domain.model.Book


@Composable
fun BookCard(
    book: Book,
    title: String,
    author: String,
    thumbnailUrl: String,
    description: String,
    onActionClick: () -> Unit,
    actionIcon: androidx.compose.ui.graphics.vector.ImageVector,
    actionContentDescription: String,
    onCardClick: () -> Unit,
    onInfoClick: (() -> Unit)?,
    onQuoteClick: () -> Unit
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
                if (description.isNotEmpty()) {
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
            Column(horizontalAlignment = Alignment.End) {
                if (actionIcon != Icons.Default.Delete) {
                    IconButton(onClick = onActionClick) {
                        Icon(
                            imageVector = actionIcon,
                            contentDescription = actionContentDescription,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                onInfoClick?.let {
                    IconButton(onClick = it) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Book Details",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
                if(onInfoClick !=null){
                    IconButton(onClick =  {
                        onQuoteClick() }){
                        Icon(
                            imageVector = Icons.Default.FormatQuote,
                            contentDescription = "Share Quote",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

            }

        }
    }
}
