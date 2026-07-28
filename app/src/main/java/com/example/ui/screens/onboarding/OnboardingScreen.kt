package com.example.ui.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BizMateLogo
import com.example.ui.theme.Indigo600
import com.example.ui.theme.SecondaryGold

@Composable
fun OnboardingScreen(
    onFinishOnboarding: (businessType: String, language: String) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedBusinessType by remember { mutableStateOf("Fashion & Apparel") }
    var selectedLanguage by remember { mutableStateOf("English") }

    val businessTypes = listOf(
        "Fashion & Apparel" to "Fashion & Clothing",
        "Beauty & Cosmetics" to "Cosmetics & Skincare",
        "Food & Restaurant" to "Food & Grocery",
        "Gadgets & Tech" to "Electronics & Tech",
        "Handmade & Crafts" to "Handmade Crafts",
        "Professional Services" to "Services & Freelance",
        "General Store" to "Online Retail Store",
        "Other Business" to "Other Enterprise"
    )

    val languages = listOf(
        "English" to "English (Global Business Default)",
        "বাংলা" to "বাংলা (Bangla Native)",
        "हिन्दी" to "हिन्दी (Hindi)"
    )

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    repeat(3) { index ->
                        val isCurrent = (index + 1) == step
                        Box(
                            modifier = Modifier
                                .height(8.dp)
                                .width(if (isCurrent) 28.dp else 8.dp)
                                .clip(CircleShape)
                                .background(
                                    if (isCurrent) Indigo600 else MaterialTheme.colorScheme.surfaceVariant
                                )
                        )
                    }
                }

                TextButton(
                    onClick = { onFinishOnboarding(selectedBusinessType, selectedLanguage) },
                    modifier = Modifier.testTag("skip_onboarding_button")
                ) {
                    Text(text = "Skip", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            AnimatedContent(
                targetState = step,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                label = "OnboardingStep"
            ) { currentStep ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (currentStep) {
                        1 -> {
                            BizMateLogo(iconSize = 72.dp, showTagline = false)
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Welcome to BizMate AI",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Your AI Business Assistant for content, customer replies, and global translation.",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                textAlign = TextAlign.Center
                            )
                        }

                        2 -> {
                            Icon(
                                imageVector = Icons.Default.Store,
                                contentDescription = "Business Category",
                                tint = Indigo600,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "What is your business category?",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Choosing your category helps BizMate AI generate targeted marketing content",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(businessTypes) { item ->
                                    val isSelected = selectedBusinessType == item.first
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedBusinessType = item.first },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                        ),
                                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
                                            brush = androidx.compose.ui.graphics.SolidColor(Indigo600)
                                        ) else null
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = item.second,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 13.sp
                                                ),
                                                modifier = Modifier.weight(1f)
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Indigo600,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        3 -> {
                            Icon(
                                imageVector = Icons.Default.Language,
                                contentDescription = "Language",
                                tint = SecondaryGold,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Select App Language",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Choose your preferred primary language for the application",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                languages.forEach { lang ->
                                    val isSelected = selectedLanguage == lang.first
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { selectedLanguage = lang.first },
                                        shape = RoundedCornerShape(14.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                        ),
                                        border = if (isSelected) CardDefaults.outlinedCardBorder().copy(
                                            brush = androidx.compose.ui.graphics.SolidColor(Indigo600)
                                        ) else null
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = lang.second,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                            )
                                            if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Indigo600
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    OutlinedButton(
                        onClick = { step -= 1 },
                        modifier = Modifier.testTag("onboarding_back_button")
                    ) {
                        Text(text = "Back")
                    }
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                Button(
                    onClick = {
                        if (step < 3) {
                            step += 1
                        } else {
                            onFinishOnboarding(selectedBusinessType, selectedLanguage)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    modifier = Modifier.testTag("onboarding_next_button")
                ) {
                    Text(
                        text = if (step == 3) "Get Started" else "Next",
                        style = MaterialTheme.typography.labelLarge
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Next",
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
