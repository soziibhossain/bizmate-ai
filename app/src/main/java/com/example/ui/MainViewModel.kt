package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.AiResult
import com.example.data.AppDatabase
import com.example.data.ContentRepository
import com.example.data.SavedContentEntity
import com.example.data.UserPreferencesEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class GenerationUiState {
    object Idle : GenerationUiState()
    object Loading : GenerationUiState()
    data class Success(val result: String) : GenerationUiState()
    data class Error(val message: String) : GenerationUiState()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    val repository = ContentRepository(db.savedContentDao(), db.userPreferencesDao())

    val userPreferences: StateFlow<UserPreferencesEntity?> = repository.userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferencesEntity()
        )

    val savedContents: StateFlow<List<SavedContentEntity>> = repository.savedContents
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _generationState = MutableStateFlow<GenerationUiState>(GenerationUiState.Idle)
    val generationState: StateFlow<GenerationUiState> = _generationState.asStateFlow()

    private val _saveNotification = MutableStateFlow<String?>(null)
    val saveNotification: StateFlow<String?> = _saveNotification.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getPreferencesSync()
        }
    }

    fun resetGenerationState() {
        _generationState.value = GenerationUiState.Idle
    }

    fun clearSaveNotification() {
        _saveNotification.value = null
    }

    fun completeOnboarding(businessType: String, language: String) {
        viewModelScope.launch {
            repository.completeOnboarding(businessType, language)
        }
    }

    fun updateProfile(userName: String, businessType: String, language: String) {
        viewModelScope.launch {
            repository.updateProfile(userName, businessType, language)
        }
    }

    fun loginUser(email: String, name: String) {
        viewModelScope.launch {
            repository.updateAuthState(true, email, name)
        }
    }

    fun logoutUser() {
        viewModelScope.launch {
            repository.updateAuthState(false, "owner@bizmate.ai", "এস এম টেক্সটাইল")
        }
    }

    fun generateSocialPost(
        businessType: String,
        productName: String,
        productDetails: String,
        price: String,
        offer: String,
        targetAudience: String,
        platform: String,
        language: String,
        tone: String
    ) {
        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading
            when (val res = repository.generateSocialPost(
                businessType, productName, productDetails, price, offer, targetAudience, platform, language, tone
            )) {
                is AiResult.Success -> _generationState.value = GenerationUiState.Success(res.data)
                is AiResult.Error -> _generationState.value = GenerationUiState.Error(res.message)
            }
        }
    }

    fun generateProductDescription(
        productName: String,
        category: String,
        features: String,
        price: String,
        targetCustomer: String,
        language: String,
        tone: String
    ) {
        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading
            when (val res = repository.generateProductDescription(
                productName, category, features, price, targetCustomer, language, tone
            )) {
                is AiResult.Success -> _generationState.value = GenerationUiState.Success(res.data)
                is AiResult.Error -> _generationState.value = GenerationUiState.Error(res.message)
            }
        }
    }

    fun generateCustomerReply(
        customerMessage: String,
        replyType: String,
        tone: String
    ) {
        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading
            when (val res = repository.generateCustomerReply(customerMessage, replyType, tone)) {
                is AiResult.Success -> _generationState.value = GenerationUiState.Success(res.data)
                is AiResult.Error -> _generationState.value = GenerationUiState.Error(res.message)
            }
        }
    }

    fun translateBusinessText(
        inputText: String,
        sourceToTarget: String,
        tone: String
    ) {
        viewModelScope.launch {
            _generationState.value = GenerationUiState.Loading
            when (val res = repository.translateBusinessText(inputText, sourceToTarget, tone)) {
                is AiResult.Success -> _generationState.value = GenerationUiState.Success(res.data)
                is AiResult.Error -> _generationState.value = GenerationUiState.Error(res.message)
            }
        }
    }

    fun saveGeneratedContent(
        toolType: String,
        title: String,
        inputText: String,
        generatedOutput: String,
        language: String
    ) {
        viewModelScope.launch {
            repository.saveContent(toolType, title, inputText, generatedOutput, language)
            _saveNotification.value = "কন্টেন্ট সফলভাবে সেভ করা হয়েছে!"
        }
    }

    fun deleteSavedContent(id: Long) {
        viewModelScope.launch {
            repository.deleteContent(id)
            _saveNotification.value = "সেভ করা তথ্য মুছে ফেলা হয়েছে।"
        }
    }

    fun toggleFavorite(id: Long, currentFav: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(id, !currentFav)
        }
    }
}
