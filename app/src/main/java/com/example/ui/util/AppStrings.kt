package com.example.ui.util

object AppStrings {
    fun welcomeMessage(name: String, language: String): String = when (language) {
        "বাংলা", "Bengali" -> "স্বাগতম, $name 👋"
        "हिन्दी", "Hindi" -> "वापसी पर स्वागत है, $name 👋"
        else -> "Welcome back, $name 👋"
    }

    fun homeSubtitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "আজ আপনার ব্যবসায় কীভাবে সাহায্য করতে পারি?"
        "हिन्दी", "Hindi" -> "আজ আপনার ব্যবসার কি সহায়তা করতে পারি?"
        else -> "How can I help your business today?"
    }

    fun aiUsageTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "আজকের ফ্রি এআই ব্যবহার"
        "हिन्दी", "Hindi" -> "दैनिक फ्री AI उपयोग"
        else -> "Daily Free AI Usage"
    }

    fun aiUsageCountText(count: Int, max: Int, language: String): String = when (language) {
        "বাংলা", "Bengali" -> "$count / $max টি ব্যবহার করা হয়েছে"
        "हिन्दी", "Hindi" -> "$count / $max उपयोग किए गए"
        else -> "$count / $max generations used today"
    }

    fun createWithAiTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "এআই দিয়ে তৈরি করুন"
        "हिन्दी", "Hindi" -> "AI के साथ बनाएं"
        else -> "Create with AI"
    }

    fun quickActionsTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "দ্রুত একশন"
        "हिन्दी", "Hindi" -> "त्वरित कार्य"
        else -> "Quick Actions"
    }

    fun recentActivityTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "সাম্প্রতিক জেনারেট করা কন্টেন্ট"
        "हिन्दी", "Hindi" -> "हाल ही के निर्माण"
        else -> "Recent Activity"
    }

    fun viewAll(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "সব দেখুন"
        "हिन्दी", "Hindi" -> "सभी देखें"
        else -> "View All"
    }

    // Tools
    fun socialPostTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "সোশ্যাল মিডিয়া পোস্ট জেনারেটর"
        "हिन्दी", "Hindi" -> "सोशल मीडिया पोस्ट जनरेटर"
        else -> "Social Media Post Generator"
    }

    fun socialPostSubtitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "Facebook, Instagram ও WhatsApp-এর জন্য সেরা পোস্ট"
        "हिन्दी", "Hindi" -> "फेसबुक और इंस्टाग्राम के लिए आकर्षक पोस्ट"
        else -> "Create engaging Facebook, Instagram & WhatsApp posts"
    }

    fun productDescTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "প্রোডাক্ট ডেসক্রিপশন"
        "हिन्दी", "Hindi" -> "उत्पाद विवरण"
        else -> "Product Description"
    }

    fun productDescSubtitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "ই-কমার্স পণ্যের সেলস ডেসক্রিপশন"
        "हिन्दी", "Hindi" -> "उच्च-रूपांतरण उत्पाद विवरण"
        else -> "Generate high-converting e-commerce descriptions"
    }

    fun customerReplyTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "কাস্টমার মেসেজ রিপ্লাই এআই"
        "हिन्दी", "Hindi" -> "ग्राहक उत्तर AI"
        else -> "Customer Reply AI"
    }

    fun customerReplySubtitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "গ্রাহকের ইনবক্স মেসেজের পেশাদার উত্তর"
        "हिन्दी", "Hindi" -> "ग्राहकों के संदेशों के पेशेवर उत्तर"
        else -> "Draft professional responses to your customers"
    }

    fun translatorTitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "বিজনেস অনুবাদক"
        "हिन्दी", "Hindi" -> "व्यापार अनुवादक"
        else -> "Business Translator"
    }

    fun translatorSubtitle(language: String): String = when (language) {
        "বাংলা", "Bengali" -> "বাণিজ্যিক মেসেজ ও ডকুমেন্টের নির্ভুল অনুবাদ"
        "हिन्दी", "Hindi" -> "व्यावसायिक संदेशों का सटीक अनुवाद"
        else -> "Translate commercial text across languages seamlessly"
    }
}
