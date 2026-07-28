package com.example.ai

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<ContentItem>,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null,
    @Json(name = "systemInstruction") val systemInstruction: ContentItem? = null
)

@JsonClass(generateAdapter = true)
data class ContentItem(
    @Json(name = "parts") val parts: List<PartItem>
)

@JsonClass(generateAdapter = true)
data class PartItem(
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.7f,
    @Json(name = "topP") val topP: Float? = 0.95f
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<CandidateItem>? = null,
    @Json(name = "error") val error: ApiErrorItem? = null
)

@JsonClass(generateAdapter = true)
data class CandidateItem(
    @Json(name = "content") val content: ContentItem? = null
)

@JsonClass(generateAdapter = true)
data class ApiErrorItem(
    @Json(name = "code") val code: Int? = null,
    @Json(name = "message") val message: String? = null
)
