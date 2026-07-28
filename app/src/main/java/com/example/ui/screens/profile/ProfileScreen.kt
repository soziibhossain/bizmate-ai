package com.example.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.data.ContentRepository
import com.example.data.UserPreferencesEntity
import com.example.ui.MainViewModel
import com.example.ui.theme.Indigo600
import com.example.ui.theme.Slate200

@Composable
fun ProfileScreen(
    userPreferences: UserPreferencesEntity?,
    viewModel: MainViewModel,
    onNavigateToAuth: () -> Unit
) {
    val context = LocalContext.current
    var userNameInput by remember(userPreferences) { mutableStateOf(userPreferences?.userName ?: "Tanvir") }
    var businessTypeInput by remember(userPreferences) { mutableStateOf(userPreferences?.businessType ?: "Fashion & Apparel") }
    var appLanguageInput by remember(userPreferences) { mutableStateOf(userPreferences?.preferredLanguage ?: "English") }

    val isLoggedIn = userPreferences?.isLoggedIn ?: false
    val dailyCount = userPreferences?.dailyAiCount ?: 0
    val maxLimit = ContentRepository.MAX_DAILY_AI_LIMIT

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Profile & Settings",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
        )

        // Profile Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Indigo600),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userNameInput.take(1).uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = userNameInput,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (isLoggedIn) (userPreferences?.userEmail ?: "owner@bizmate.ai") else "Guest User",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        // Section: Business Profile
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Business Details",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                OutlinedTextField(
                    value = userNameInput,
                    onValueChange = { userNameInput = it },
                    label = { Text("Business / Owner Name") },
                    leadingIcon = { Icon(Icons.Default.Store, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_name_input"),
                    singleLine = true
                )

                OutlinedTextField(
                    value = businessTypeInput,
                    onValueChange = { businessTypeInput = it },
                    label = { Text("Business Category") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_category_input"),
                    singleLine = true
                )

                Button(
                    onClick = {
                        viewModel.updateProfile(userNameInput, businessTypeInput, appLanguageInput)
                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("save_profile_button")
                ) {
                    Icon(Icons.Default.Save, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section: App Preferences (Languages)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = Indigo600)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "App Interface Language",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("English", "বাংলা", "हिन्दी").forEach { lang ->
                        FilterChip(
                            selected = appLanguageInput == lang,
                            onClick = {
                                appLanguageInput = lang
                                viewModel.updateProfile(userNameInput, businessTypeInput, lang)
                                Toast.makeText(context, "App language set to $lang", Toast.LENGTH_SHORT).show()
                            },
                            label = { Text(lang) }
                        )
                    }
                }
            }
        }

        // Section: AI Usage
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Indigo600)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Daily AI Usage",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Text(
                    text = "$dailyCount / $maxLimit Free AI generations used today",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }

        // Section: Account Actions
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                if (isLoggedIn) {
                    OutlinedButton(
                        onClick = { viewModel.logoutUser() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("logout_button")
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Logout",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign Out", color = MaterialTheme.colorScheme.error)
                    }
                } else {
                    Button(
                        onClick = onNavigateToAuth,
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("login_screen_button")
                    ) {
                        Text("Sign In / Sign Up")
                    }
                }
            }
        }

        // Section: About & Support
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = Slate200, shape = RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "About BizMate AI",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { Toast.makeText(context, "Privacy Policy", Toast.LENGTH_SHORT).show() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.PrivacyTip, contentDescription = null, tint = Indigo600)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Privacy Policy & Security", style = MaterialTheme.typography.bodyMedium)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { Toast.makeText(context, "Help & Support", Toast.LENGTH_SHORT).show() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.HelpOutline, contentDescription = null, tint = Indigo600)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text("Help & Customer Support", style = MaterialTheme.typography.bodyMedium)
                }

                Text(
                    text = "BizMate AI v2.0 • Powered by AI",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                )
            }
        }
    }
}
