package com.example.ui.screens.home

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ContentRepository
import com.example.data.SavedContentEntity
import com.example.data.UserPreferencesEntity
import com.example.ui.theme.Indigo500
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

data class ToolItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badgeBgColor: Color,
    val badgeIconColor: Color,
    val tag: String
)

@Composable
fun HomeScreen(
    userPreferences: UserPreferencesEntity?,
    recentContents: List<SavedContentEntity>,
    onSelectTool: (String) -> Unit,
    onViewAllHistory: () -> Unit
) {
    val userName = userPreferences?.userName ?: "Tanvir"
    val preferredLanguage = userPreferences?.preferredLanguage ?: "English"
    val dailyCount = userPreferences?.dailyAiCount ?: 0
    val maxLimit = ContentRepository.MAX_DAILY_AI_LIMIT

    val tools = listOf(
        ToolItem(
            id = "SOCIAL_POST",
            title = AppStrings.socialPostTitle(preferredLanguage),
            description = AppStrings.socialPostSubtitle(preferredLanguage),
            icon = Icons.Default.Campaign,
            badgeBgColor = ToolBlueBg,
            badgeIconColor = ToolBlueIcon,
            tag = "tool_card_social_post"
        ),
        ToolItem(
            id = "PRODUCT_DESC",
            title = AppStrings.productDescTitle(preferredLanguage),
            description = AppStrings.productDescSubtitle(preferredLanguage),
            icon = Icons.Default.Description,
            badgeBgColor = ToolAmberBg,
            badgeIconColor = ToolAmberIcon,
            tag = "tool_card_product_desc"
        ),
        ToolItem(
            id = "CUSTOMER_REPLY",
            title = AppStrings.customerReplyTitle(preferredLanguage),
            description = AppStrings.customerReplySubtitle(preferredLanguage),
            icon = Icons.Default.Chat,
            badgeBgColor = ToolEmeraldBg,
            badgeIconColor = ToolEmeraldIcon,
            tag = "tool_card_customer_reply"
        ),
        ToolItem(
            id = "TRANSLATION",
            title = AppStrings.translatorTitle(preferredLanguage),
            description = AppStrings.translatorSubtitle(preferredLanguage),
            icon = Icons.Default.Translate,
            badgeBgColor = ToolPurpleBg,
            badgeIconColor = ToolPurpleIcon,
            tag = "tool_card_translator"
        )
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Welcome Header
        item {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = AppStrings.welcomeMessage(userName, preferredLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = AppStrings.homeSubtitle(preferredLanguage),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Usage Limit Card (50 / day)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
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
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = "Usage",
                                tint = Indigo600,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = AppStrings.aiUsageTitle(preferredLanguage),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        Text(
                            text = AppStrings.aiUsageCountText(dailyCount, maxLimit, preferredLanguage),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Indigo600
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { (dailyCount.toFloat() / maxLimit.toFloat()).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Indigo600,
                        trackColor = Slate100
                    )
                }
            }
        }

        // Quick Actions Row
        item {
            Column {
                Text(
                    text = AppStrings.quickActionsTitle(preferredLanguage),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        QuickActionChip(
                            label = if (preferredLanguage == "English") "New Post" else "নতুন পোস্ট",
                            icon = Icons.Default.Add,
                            onClick = { onSelectTool("SOCIAL_POST") }
                        )
                    }
                    item {
                        QuickActionChip(
                            label = if (preferredLanguage == "English") "Reply to Customer" else "কাস্টমার রিপ্লাই",
                            icon = Icons.Default.Chat,
                            onClick = { onSelectTool("CUSTOMER_REPLY") }
                        )
                    }
                    item {
                        QuickActionChip(
                            label = if (preferredLanguage == "English") "Translate Text" else "অনুবাদ করুন",
                            icon = Icons.Default.Translate,
                            onClick = { onSelectTool("TRANSLATION") }
                        )
                    }
                }
            }
        }

        // "Create with AI" Section Header
        item {
            Text(
                text = AppStrings.createWithAiTitle(preferredLanguage),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // 4 AI Tool Cards
        items(tools) { tool ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectTool(tool.id) }
                    .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp))
                    .testTag(tool.tag),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(tool.badgeBgColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = tool.icon,
                            contentDescription = tool.title,
                            tint = tool.badgeIconColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = tool.title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = tool.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 13.sp
                            )
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Open",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Recent Activity Section
        if (recentContents.isNotEmpty()) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.recentActivityTitle(preferredLanguage),
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = AppStrings.viewAll(preferredLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Indigo600
                        ),
                        modifier = Modifier.clickable { onViewAllHistory() }
                    )
                }
            }

            items(recentContents.take(3)) { saved ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(
                            text = saved.title,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = saved.generatedOutput,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            ),
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionChip(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Slate100)
            .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Indigo600,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Slate600
                )
            )
        }
    }
}
