package com.example.ui.screens.history

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.SavedContentEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo600
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.Slate200

@Composable
fun HistoryScreen(
    viewModel: MainViewModel,
    savedList: List<SavedContentEntity>
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = savedList.filter { item ->
        val matchesQuery = searchQuery.isBlank() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.generatedOutput.contains(searchQuery, ignoreCase = true)
        val matchesFilter = when (selectedFilter) {
            "ALL" -> true
            "SOCIAL_POST" -> item.toolType == "SOCIAL_POST"
            "PRODUCT_DESC" -> item.toolType == "PRODUCT_DESC"
            "CUSTOMER_REPLY" -> item.toolType == "CUSTOMER_REPLY"
            "TRANSLATION" -> item.toolType == "TRANSLATION"
            else -> true
        }
        matchesQuery && matchesFilter
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "Saved History",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Manage and export your generated AI content",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Search history...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("history_search_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val filters = listOf(
                "ALL" to "All Content",
                "SOCIAL_POST" to "Social Posts",
                "PRODUCT_DESC" to "Descriptions",
                "CUSTOMER_REPLY" to "Replies",
                "TRANSLATION" to "Translations"
            )
            items(filters) { f ->
                FilterChip(
                    selected = selectedFilter == f.first,
                    onClick = { selectedFilter = f.first },
                    label = { Text(f.second, fontSize = 12.sp) }
                )
            }
        }

        if (filteredList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(56.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No saved content yet",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Generated posts, descriptions, and replies saved from AI tools will appear here.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredList) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp))
                            .testTag("history_card_${item.id}"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Indigo100)
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = when (item.toolType) {
                                                "SOCIAL_POST" -> "Social Post"
                                                "PRODUCT_DESC" -> "Product Desc"
                                                "CUSTOMER_REPLY" -> "Customer Reply"
                                                "TRANSLATION" -> "Translation"
                                                else -> "AI Tool"
                                            },
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Indigo600,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = item.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                IconButton(onClick = {
                                    viewModel.toggleFavorite(item.id, item.isFavorite)
                                }) {
                                    Icon(
                                        imageVector = if (item.isFavorite) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                        contentDescription = "Favorite",
                                        tint = if (item.isFavorite) SecondaryGold else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = item.generatedOutput,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                ),
                                maxLines = 5
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("BizMate AI", item.generatedOutput))
                                    Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                }

                                IconButton(onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, item.generatedOutput)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = "Share")
                                }

                                IconButton(onClick = {
                                    viewModel.deleteSavedContent(item.id)
                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
