package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferencesDao {
    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getUserPreferences(): Flow<UserPreferencesEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun getUserPreferencesSync(): UserPreferencesEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserPreferences(preferences: UserPreferencesEntity)

    @Query("UPDATE user_preferences SET dailyAiCount = :count, lastUsageDate = :date WHERE id = 1")
    suspend fun updateUsage(count: Int, date: String)

    @Query("UPDATE user_preferences SET userName = :name, businessType = :type, preferredLanguage = :lang WHERE id = 1")
    suspend fun updateProfile(name: String, type: String, lang: String)

    @Query("UPDATE user_preferences SET isOnboardingCompleted = :completed WHERE id = 1")
    suspend fun updateOnboarding(completed: Boolean)

    @Query("UPDATE user_preferences SET isLoggedIn = :loggedIn, userEmail = :email, userName = :name WHERE id = 1")
    suspend fun updateAuthState(loggedIn: Boolean, email: String, name: String)
}
