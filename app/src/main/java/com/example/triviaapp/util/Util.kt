package com.example.triviaapp.util

import androidx.core.text.HtmlCompat

object Util {
    fun decodeHTMLText(string: String): String{
//        val rawText = string
//        val question = rawText
//            .replace("&#039;", "'")
//            .replace("&quot;", "\"")
//            .replace("&amp;", "&")
//        return question

        return HtmlCompat.fromHtml(string, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
}