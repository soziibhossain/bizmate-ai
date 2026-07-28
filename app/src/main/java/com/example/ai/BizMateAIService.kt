package com.example.ai

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class AiResult<out T> {
    data class Success<out T>(val data: T) : AiResult<T>()
    data class Error(val message: String) : AiResult<Nothing>()
}

class BizMateAIService {

    private val systemInstructionText = """
        You are BizMate AI, an elite AI business assistant designed for global small enterprise owners, e-commerce stores, social media sellers, and local businesses.
        
        System Rules:
        - Understand business context, shopping preferences, marketing psychology, and customer communication tone.
        - Produce natural, high-converting content in the requested target language (English, Bangla, Hindi, Spanish, etc.).
        - NEVER fabricate or invent product specifications that were not provided by the user.
        - Maintain a highly polite, respectful, and customer-centric tone.
        - Format outputs clearly with sections, bold headers, bullet points, emojis, and actionable calls to action.
    """.trimIndent()

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
    ): AiResult<String> = withContext(Dispatchers.IO) {
        val prompt = """
            [Task: Generate $platform Social Media Post]
            Business Category: $businessType
            Product/Service: $productName
            Product Details: $productDetails
            Price: ${if (price.isNotBlank()) price else "Inquire via direct message"}
            Offer/Discount: ${if (offer.isNotBlank()) offer else "N/A"}
            Target Audience: ${if (targetAudience.isNotBlank()) targetAudience else "General customers"}
            Platform: $platform
            Tone: $tone
            Language: $language

            Instructions:
            Create a highly engaging, high-converting social media post optimized for $platform in $language.
            You MUST include ALL of the following 4 sections clearly labeled:
            1. Headline
            2. Main Post
            3. Call to Action
            4. Hashtags
        """.trimIndent()

        callGemini(prompt)
    }

    suspend fun generateProductDescription(
        productName: String,
        category: String,
        features: String,
        price: String,
        targetCustomer: String,
        language: String,
        tone: String
    ): AiResult<String> = withContext(Dispatchers.IO) {
        val prompt = """
            [Task: Generate Product Description]
            Product Name: $productName
            Category: $category
            Key Features/Materials: $features
            Price: ${if (price.isNotBlank()) price else "N/A"}
            Target Customer: ${if (targetCustomer.isNotBlank()) targetCustomer else "General buyers"}
            Language: $language
            Tone: $tone

            Instructions:
            Generate a detailed, high-converting product description for e-commerce listing in $language.
            Structure the output clearly:
            1. 🌟 Catchy Product Title
            2. 📝 Short Summary
            3. ✨ Detailed Description
            4. 🔍 Key Features (bullet points)
            5. 🛒 Call to Action
        """.trimIndent()

        callGemini(prompt)
    }

    suspend fun generateCustomerReply(
        customerMessage: String,
        replyType: String,
        tone: String,
        language: String = "English"
    ): AiResult<String> = withContext(Dispatchers.IO) {
        val prompt = """
            [Task: Generate Professional Customer Message Reply]
            Customer Message: "$customerMessage"
            Context / Reply Type: $replyType
            Tone: $tone
            Language: $language

            Instructions:
            Write a respectful, helpful, and polite response for customer service in $language.
            Provide 2 variations:
            Option 1: Short & Direct
            Option 2: Detailed & Warm
        """.trimIndent()

        callGemini(prompt)
    }

    suspend fun translateBusinessText(
        inputText: String,
        sourceToTarget: String,
        tone: String
    ): AiResult<String> = withContext(Dispatchers.IO) {
        val prompt = """
            [Task: Business Communication Translation]
            Direction: $sourceToTarget
            Tone: $tone
            Input Text: "$inputText"

            Instructions:
            Translate the business message keeping natural commercial context, proper honorifics, and fluent phrasing.
            Provide:
            1. 🎯 Professional Business Translation
            2. 💡 Key Nuances & Tone Note
        """.trimIndent()

        callGemini(prompt)
    }

    suspend fun callGemini(userPrompt: String): AiResult<String> = withContext(Dispatchers.IO) {
        val apiKey = GeminiApiClient.getApiKey()
        if (apiKey.isBlank()) {
            Log.e("BizMateAI", "AI Request Failed: GEMINI_API_KEY missing")
            return@withContext AiResult.Error("Gemini API key is missing. Please configure GEMINI_API_KEY in AI Studio Secrets.")
        }

        try {
            Log.d("BizMateAI", "AI Request Started. Prompt snippet: ${userPrompt.take(60)}...")
            val request = GeminiRequest(
                contents = listOf(
                    ContentItem(parts = listOf(PartItem(text = userPrompt)))
                ),
                systemInstruction = ContentItem(parts = listOf(PartItem(text = systemInstructionText)))
            )

            val response = GeminiApiClient.service.generateContent(apiKey, request)
            val statusCode = response.code()

            Log.d("BizMateAI", "AI Response HTTP Status Code: $statusCode")

            if (response.isSuccessful) {
                val body = response.body()
                val errorMsg = body?.error?.message
                if (!errorMsg.isNullOrEmpty()) {
                    Log.e("BizMateAI", "AI Response contained error field: $errorMsg")
                    return@withContext AiResult.Error("Something went wrong. Please try again.")
                }

                val textResult = body?.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (textResult.isNullOrBlank()) {
                    Log.e("BizMateAI", "AI Response body candidates/text empty")
                    return@withContext AiResult.Error("No response received from AI. Please try again.")
                }

                Log.d("BizMateAI", "AI Request Success, generated text length: ${textResult.length}")
                return@withContext AiResult.Success(textResult.trim())
            } else {
                val errBody = response.errorBody()?.string() ?: ""
                Log.e("BizMateAI", "AI Request HTTP Error $statusCode: $errBody")

                val errorMessage = when (statusCode) {
                    401, 403 -> "AI API authentication failed. Please check the API key configuration."
                    429 -> "AI usage limit reached. Please try again later."
                    in 500..599 -> "AI service is temporarily unavailable. Please try again later."
                    else -> "Something went wrong. Please try again."
                }
                return@withContext AiResult.Error(errorMessage)
            }
        } catch (e: java.io.IOException) {
            Log.e("BizMateAI", "AI Request Network IO Exception: ${e.message}")
            return@withContext AiResult.Error("Unable to connect to AI. Please check your internet connection.")
        } catch (e: Exception) {
            Log.e("BizMateAI", "AI Request Exception: ${e.message}", e)
            return@withContext AiResult.Error("Something went wrong. Please try again.")
        }
    }
}

