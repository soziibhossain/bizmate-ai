package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import android.util.Log
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.GenerationUiState
import com.example.ui.MainViewModel
import com.example.ui.theme.Indigo100
import com.example.ui.theme.Indigo600
import com.example.ui.theme.SecondaryGold
import com.example.ui.theme.Slate200

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ToolDetailScreen(
    toolType: String,
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefState by viewModel.userPreferences.collectAsState()

    // Social Media Post inputs
    var productName by remember { mutableStateOf("") }
    var productDetails by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var offer by remember { mutableStateOf("") }
    var targetAudience by remember { mutableStateOf("") }
    var platform by remember { mutableStateOf("Facebook") }
    var language by remember { mutableStateOf(prefState?.preferredLanguage ?: "English") }
    var tone by remember { mutableStateOf("Professional") }

    // Product Description inputs
    var category by remember { mutableStateOf("") }
    var targetCustomer by remember { mutableStateOf("") }

    // Customer Reply inputs
    var customerMessage by remember { mutableStateOf("") }
    var replyType by remember { mutableStateOf("General Inquiry") }

    // Business Translation inputs
    var translationText by remember { mutableStateOf("") }
    var sourceLanguage by remember { mutableStateOf("English") }
    var targetLanguage by remember { mutableStateOf("Bangla") }

    var editableOutput by remember { mutableStateOf("") }
    val genState by viewModel.generationState.collectAsState()

    LaunchedEffect(genState) {
        val current = genState
        if (current is GenerationUiState.Success) {
            editableOutput = current.result
        }
    }

    val (toolTitle, toolSubtitle) = when (toolType) {
        "SOCIAL_POST" -> "Social Media Post" to "Create engaging content for your business."
        "PRODUCT_DESC" -> "Product Description" to "Generate high-converting e-commerce listings."
        "CUSTOMER_REPLY" -> "Customer Reply AI" to "Generate a professional response to your customer."
        "TRANSLATION" -> "Business Translator" to "Translate commercial text accurately across languages."
        else -> "AI Business Tool" to "Powered by BizMate AI"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = toolTitle,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = toolSubtitle,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("tool_detail_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when (toolType) {
                        "SOCIAL_POST" -> {
                            OutlinedTextField(
                                value = productName,
                                onValueChange = { productName = it },
                                label = { Text("Product or Service Name *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_product_name"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = productDetails,
                                onValueChange = { productDetails = it },
                                label = { Text("Product Details & Features *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .testTag("input_product_details")
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Price (Optional)") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = offer,
                                    onValueChange = { offer = it },
                                    label = { Text("Offer / Discount") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            OutlinedTextField(
                                value = targetAudience,
                                onValueChange = { targetAudience = it },
                                label = { Text("Target Audience (e.g. Young professionals)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Text("Platform:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("Facebook", "Instagram", "WhatsApp").forEach { item ->
                                    FilterChip(
                                        selected = platform == item,
                                        onClick = { platform = item },
                                        label = { Text(item) }
                                    )
                                }
                            }

                            Text("Tone:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Professional", "Friendly", "Casual", "Luxury", "Promotional").forEach { t ->
                                    FilterChip(
                                        selected = tone == t,
                                        onClick = { tone = t },
                                        label = { Text(t, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        "PRODUCT_DESC" -> {
                            OutlinedTextField(
                                value = productName,
                                onValueChange = { productName = it },
                                label = { Text("Product Name *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_product_name"),
                                singleLine = true
                            )

                            OutlinedTextField(
                                value = category,
                                onValueChange = { category = it },
                                label = { Text("Category (e.g., Electronics, Fashion)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            OutlinedTextField(
                                value = productDetails,
                                onValueChange = { productDetails = it },
                                label = { Text("Product Features & Specifications *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp)
                                    .testTag("input_product_features")
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Price") },
                                    modifier = Modifier.weight(1f)
                                )
                                OutlinedTextField(
                                    value = targetCustomer,
                                    onValueChange = { targetCustomer = it },
                                    label = { Text("Target Customer") },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Text("Tone:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Professional", "Friendly", "Persuasive", "Luxury", "Technical").forEach { t ->
                                    FilterChip(
                                        selected = tone == t,
                                        onClick = { tone = t },
                                        label = { Text(t, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        "CUSTOMER_REPLY" -> {
                            OutlinedTextField(
                                value = customerMessage,
                                onValueChange = { customerMessage = it },
                                label = { Text("Paste your customer's message here... *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(110.dp)
                                    .testTag("input_customer_message")
                            )

                            Text("Reply Type:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf(
                                    "General Inquiry", "Price Question", "Price Negotiation",
                                    "Order Confirmation", "Delivery", "Complaint",
                                    "Refund / Exchange", "Thank You"
                                ).forEach { item ->
                                    FilterChip(
                                        selected = replyType == item,
                                        onClick = { replyType = item },
                                        label = { Text(item, fontSize = 11.sp) }
                                    )
                                }
                            }

                            Text("Tone:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Friendly", "Professional", "Polite", "Short").forEach { t ->
                                    FilterChip(
                                        selected = tone == t,
                                        onClick = { tone = t },
                                        label = { Text(t, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }

                        "TRANSLATION" -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "$sourceLanguage → $targetLanguage",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }

                                OutlinedButton(onClick = {
                                    val temp = sourceLanguage
                                    sourceLanguage = targetLanguage
                                    targetLanguage = temp
                                }) {
                                    Icon(Icons.Default.SwapHoriz, contentDescription = "Swap Languages")
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Swap")
                                }
                            }

                            OutlinedTextField(
                                value = translationText,
                                onValueChange = { translationText = it },
                                label = { Text("Enter business text to translate *") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .testTag("input_translation_text")
                            )

                            Text("Target Language:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("English", "Bangla", "Hindi", "Spanish").forEach { lang ->
                                    FilterChip(
                                        selected = targetLanguage == lang,
                                        onClick = { targetLanguage = lang },
                                        label = { Text(lang, fontSize = 11.sp) }
                                    )
                                }
                            }

                            Text("Tone:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                listOf("Professional", "Friendly", "Formal").forEach { t ->
                                    FilterChip(
                                        selected = tone == t,
                                        onClick = { tone = t },
                                        label = { Text(t, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }

                    // Output Language Selector (Common for Post, Product Desc, Reply)
                    if (toolType != "TRANSLATION") {
                        Text("Output Language:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            listOf("English", "Bangla", "Hindi", "Spanish").forEach { lang ->
                                FilterChip(
                                    selected = language == lang,
                                    onClick = { language = lang },
                                    label = { Text(lang, fontSize = 11.sp) }
                                )
                            }
                        }
                    }

                    // Main Action Button
                    Button(
                        onClick = {
                            Log.d("BizMateAI", "Generate button clicked for toolType: $toolType")
                            when (toolType) {
                                "SOCIAL_POST" -> viewModel.generateSocialPost(
                                    prefState?.businessType ?: "Business",
                                    productName, productDetails, price, offer, targetAudience, platform, language, tone
                                )
                                "PRODUCT_DESC" -> viewModel.generateProductDescription(
                                    productName, category.ifBlank { "General" }, productDetails, price, targetCustomer, language, tone
                                )
                                "CUSTOMER_REPLY" -> viewModel.generateCustomerReply(
                                    customerMessage, replyType, tone
                                )
                                "TRANSLATION" -> viewModel.translateBusinessText(
                                    translationText, "$sourceLanguage -> $targetLanguage", tone
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Indigo600),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("generate_ai_button"),
                        enabled = genState !is GenerationUiState.Loading
                    ) {
                        if (genState is GenerationUiState.Loading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Creating your post with AI...")
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = SecondaryGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when (toolType) {
                                    "SOCIAL_POST" -> "Generate Post"
                                    "PRODUCT_DESC" -> "Generate Description"
                                    "CUSTOMER_REPLY" -> "Generate Reply"
                                    "TRANSLATION" -> "Translate"
                                    else -> "Generate"
                                },
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Error Display Card
            if (genState is GenerationUiState.Error) {
                val err = (genState as GenerationUiState.Error).message
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // AI Output Card
            if (editableOutput.isNotBlank() || genState is GenerationUiState.Success) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = Slate200,
                            shape = RoundedCornerShape(20.dp)
                        ),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Indigo100),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Indigo600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Generated Result",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }

                            Row {
                                // Regenerate Action
                                IconButton(onClick = {
                                    when (toolType) {
                                        "SOCIAL_POST" -> viewModel.generateSocialPost(
                                            prefState?.businessType ?: "Business",
                                            productName, productDetails, price, offer, targetAudience, platform, language, tone
                                        )
                                        "PRODUCT_DESC" -> viewModel.generateProductDescription(
                                            productName, category.ifBlank { "General" }, productDetails, price, targetCustomer, language, tone
                                        )
                                        "CUSTOMER_REPLY" -> viewModel.generateCustomerReply(
                                            customerMessage, replyType, tone
                                        )
                                        "TRANSLATION" -> viewModel.translateBusinessText(
                                            translationText, "$sourceLanguage -> $targetLanguage", tone
                                        )
                                    }
                                }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = Indigo600)
                                }

                                // Copy Action
                                IconButton(onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("BizMate AI", editableOutput))
                                    Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.ContentCopy, contentDescription = "Copy")
                                }

                                // Share Action
                                IconButton(onClick = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, editableOutput)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "Share via"))
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = "Share")
                                }

                                // Save Action
                                IconButton(onClick = {
                                    viewModel.saveGeneratedContent(
                                        toolType = toolType,
                                        title = if (productName.isNotBlank()) productName else toolTitle,
                                        inputText = customerMessage.ifBlank { productDetails.ifBlank { translationText } },
                                        generatedOutput = editableOutput,
                                        language = language
                                    )
                                    Toast.makeText(context, "Saved to History!", Toast.LENGTH_SHORT).show()
                                }) {
                                    Icon(Icons.Default.Bookmark, contentDescription = "Save")
                                }

                                // Clear Action (for Translator or Output)
                                IconButton(onClick = {
                                    editableOutput = ""
                                    viewModel.resetGenerationState()
                                }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                                }
                            }
                        }

                        OutlinedTextField(
                            value = editableOutput,
                            onValueChange = { editableOutput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .testTag("generated_output_text"),
                            textStyle = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}
