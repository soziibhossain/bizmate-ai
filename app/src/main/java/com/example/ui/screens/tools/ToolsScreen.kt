package com.example.ui.screens.tools

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.UserPreferencesEntity
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate100
import com.example.ui.theme.Slate200
import com.example.ui.theme.Slate600
import com.example.ui.theme.ToolAmberBg
import com.example.ui.theme.ToolAmberIcon
import com.example.ui.theme.ToolBlueBg
import com.example.ui.theme.ToolBlueIcon
import com.example.ui.theme.ToolEmeraldBg
import com.example.ui.theme.ToolEmeraldIcon
import com.example.ui.theme.ToolPurpleBg
import com.example.ui.theme.ToolPurpleIcon
import com.example.ui.util.AppStrings

data class ToolLibraryItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String, // "CONTENT" or "COMMUNICATION"
    val icon: ImageVector,
    val badgeBgColor: Color,
    val badgeIconColor: Color,
    val tag: String
)

@Composable
fun ToolsScreen(
    userPreferences: UserPreferencesEntity?,
    onSelectTool: (String) -> Unit
) {
    val preferredLanguage = userPreferences?.preferredLanguage ?: "English"
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    val allTools = remember(preferredLanguage) {
        listOf(
            ToolLibraryItem(
                id = "SOCIAL_POST",
                title = AppStrings.socialPostTitle(preferredLanguage),
                description = AppStrings.socialPostSubtitle(preferredLanguage),
                category = "CONTENT",
                icon = Icons.Default.Campaign,
                badgeBgColor = ToolBlueBg,
                badgeIconColor = ToolBlueIcon,
                tag = "lib_tool_social_post"
            ),
            ToolLibraryItem(
                id = "PRODUCT_DESC",
                title = AppStrings.productDescTitle(preferredLanguage),
                description = AppStrings.productDescSubtitle(preferredLanguage),
                category = "CONTENT",
                icon = Icons.Default.Description,
                badgeBgColor = ToolAmberBg,
                badgeIconColor = ToolAmberIcon,
                tag = "lib_tool_product_desc"
            ),
            ToolLibraryItem(
                id = "CUSTOMER_REPLY",
                title = AppStrings.customerReplyTitle(preferredLanguage),
                description = AppStrings.customerReplySubtitle(preferredLanguage),
                category = "COMMUNICATION",
                icon = Icons.Default.Chat,
                badgeBgColor = ToolEmeraldBg,
                badgeIconColor = ToolEmeraldIcon,
                tag = "lib_tool_customer_reply"
            ),
            ToolLibraryItem(
                id = "TRANSLATION",
                title = AppStrings.translatorTitle(preferredLanguage),
                description = AppStrings.translatorSubtitle(preferredLanguage),
                category = "COMMUNICATION",
                icon = Icons.Default.Translate,
                badgeBgColor = ToolPurpleBg,
                badgeIconColor = ToolPurpleIcon,
                tag = "lib_tool_translator"
            )
        )
    }

    val contentTools = remember(allTools) { allTools.filter { it.category == "CONTENT" } }
    val commTools = remember(allTools) { allTools.filter { it.category == "COMMUNICATION" } }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Library Header
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = "AI Tools Suite",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Choose the right AI tool to power your business workflow",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Category Filter Chips
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filters = listOf(
                    "ALL" to "All Tools",
                    "CONTENT" to "Content Creation",
                    "COMMUNICATION" to "Communication"
                )
                items(filters) { (key, label) ->
                    val isSelected = selectedCategoryFilter == key
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Indigo600 else Slate100)
                            .border(
                                width = 1.dp,
                                color = if (isSelected) Indigo600 else Slate200,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .clickable { selectedCategoryFilter = key }
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Slate600
                            )
                        )
                    }
                }
            }
        }

        // CONTENT Category
        if (selectedCategoryFilter == "ALL" || selectedCategoryFilter == "CONTENT") {
            item {
                Text(
                    text = "CONTENT GENERATION",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Indigo600
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(contentTools) { item ->
                ToolLibraryCard(item = item, onClick = { onSelectTool(item.id) })
            }
        }

        // COMMUNICATION Category
        if (selectedCategoryFilter == "ALL" || selectedCategoryFilter == "COMMUNICATION") {
            item {
                Text(
                    text = "BUSINESS COMMUNICATION",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Indigo600
                    ),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            items(commTools) { item ->
                ToolLibraryCard(item = item, onClick = { onSelectTool(item.id) })
            }
        }
    }
}

@Composable
fun ToolLibraryCard(
    item: ToolLibraryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp))
            .testTag(item.tag),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(item.badgeBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = item.badgeIconColor,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Slate100),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Launch Tool",
                    tint = Indigo600,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
