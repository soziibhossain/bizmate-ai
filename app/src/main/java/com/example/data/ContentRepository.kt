package com.example.data

import com.example.ai.AiResult
import com.example.ai.BizMateAIService
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ContentRepository(
    private val savedContentDao: SavedContentDao,
    private val userPreferencesDao: UserPreferencesDao,
    private val aiService: BizMateAIService = BizMateAIService()
) {
    val savedContents: Flow<List<SavedContentEntity>> = savedContentDao.getAllSavedContent()
    val userPreferences: Flow<UserPreferencesEntity?> = userPreferencesDao.getUserPreferences()

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    suspend fun getPreferencesSync(): UserPreferencesEntity {
        var pref = userPreferencesDao.getUserPreferencesSync()
        if (pref == null) {
            pref = UserPreferencesEntity()
            userPreferencesDao.saveUserPreferences(pref)
        }
        val today = getTodayDateString()
        if (pref.lastUsageDate != today) {
            val updated = pref.copy(dailyAiCount = 0, lastUsageDate = today)
            userPreferencesDao.saveUserPreferences(updated)
            return updated
        }
        return pref
    }

    companion object {
        const val MAX_DAILY_AI_LIMIT = 50
    }

    suspend fun canUseAi(): Pair<Boolean, Int> {
        val pref = getPreferencesSync()
        val currentCount = pref.dailyAiCount
        return Pair(currentCount < MAX_DAILY_AI_LIMIT, currentCount)
    }

    suspend fun incrementAiUsage() {
        val pref = getPreferencesSync()
        val today = getTodayDateString()
        val newCount = pref.dailyAiCount + 1
        userPreferencesDao.updateUsage(newCount, today)
    }

    suspend fun saveContent(
        toolType: String,
        title: String,
        inputText: String,
        generatedOutput: String,
        language: String
    ): Long {
        return savedContentDao.insertContent(
            SavedContentEntity(
                toolType = toolType,
                title = title,
                inputText = inputText,
                generatedOutput = generatedOutput,
                language = language
            )
        )
    }

    suspend fun deleteContent(id: Long) {
        savedContentDao.deleteContentById(id)
    }

    suspend fun toggleFavorite(id: Long, isFav: Boolean) {
        savedContentDao.updateFavorite(id, isFav)
    }

    suspend fun completeOnboarding(businessType: String, language: String) {
        val pref = getPreferencesSync()
        val updated = pref.copy(
            businessType = businessType,
            preferredLanguage = language,
            isOnboardingCompleted = true
        )
        userPreferencesDao.saveUserPreferences(updated)
    }

    suspend fun updateProfile(userName: String, businessType: String, language: String) {
        userPreferencesDao.updateProfile(userName, businessType, language)
    }

    suspend fun updateAuthState(isLoggedIn: Boolean, email: String, name: String) {
        userPreferencesDao.updateAuthState(isLoggedIn, email, name)
    }

    suspend fun generateSocialPost(
        businessType: String,
        productName: String,
        productDetails: String,
        price: String,
        offer: String,
        targetAudience: String,
        platform: String,
        language: String,
        tone: String
    ): AiResult<String> {
        val (canUse, currentCount) = canUseAi()
        if (!canUse) {
            return AiResult.Error("Daily free AI limit reached ($currentCount/$MAX_DAILY_AI_LIMIT generations). Please try again tomorrow.")
        }
        val result = aiService.generateSocialPost(
            businessType, productName, productDetails, price, offer, targetAudience, platform, language, tone
        )
        if (result is AiResult.Success) {
            incrementAiUsage()
        }
        return result
    }

    suspend fun generateProductDescription(
        productName: String,
        category: String,
        features: String,
        price: String,
        targetCustomer: String,
        language: String,
        tone: String
    ): AiResult<String> {
        val (canUse, currentCount) = canUseAi()
        if (!canUse) {
            return AiResult.Error("Daily free AI limit reached ($currentCount/$MAX_DAILY_AI_LIMIT generations). Please try again tomorrow.")
        }
        val result = aiService.generateProductDescription(
            productName, category, features, price, targetCustomer, language, tone
        )
        if (result is AiResult.Success) {
            incrementAiUsage()
        }
        return result
    }

    suspend fun generateCustomerReply(
        customerMessage: String,
        replyType: String,
        tone: String
    ): AiResult<String> {
        val (canUse, currentCount) = canUseAi()
        if (!canUse) {
            return AiResult.Error("Daily free AI limit reached ($currentCount/$MAX_DAILY_AI_LIMIT generations). Please try again tomorrow.")
        }
        val result = aiService.generateCustomerReply(customerMessage, replyType, tone)
        if (result is AiResult.Success) {
            incrementAiUsage()
        }
        return result
    }

    suspend fun translateBusinessText(
        inputText: String,
        sourceToTarget: String,
        tone: String
    ): AiResult<String> {
        val (canUse, currentCount) = canUseAi()
        if (!canUse) {
            return AiResult.Error("Daily free AI limit reached ($currentCount/$MAX_DAILY_AI_LIMIT generations). Please try again tomorrow.")
        }
        val result = aiService.translateBusinessText(inputText, sourceToTarget, tone)
        if (result is AiResult.Success) {
            incrementAiUsage()
        }
        return result
    }
}
