package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_content")
data class SavedContentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val toolType: String, // SOCIAL_POST, PRODUCT_DESCRIPTION, CUSTOMER_REPLY, TRANSLATION
    val title: String,
    val inputText: String,
    val generatedOutput: String,
    val language: String,
    val createdAt: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
