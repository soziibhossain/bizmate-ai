package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class UserPreferencesEntity(
    @PrimaryKey val id: Int = 1,
    val userName: String = "Tanvir",
    val businessType: String = "Fashion & Apparel",
    val preferredLanguage: String = "English",
    val isOnboardingCompleted: Boolean = false,
    val isLoggedIn: Boolean = false,
    val userEmail: String = "owner@bizmate.ai",
    val dailyAiCount: Int = 0,
    val lastUsageDate: String = "",
    val isDarkMode: Boolean = false
)
