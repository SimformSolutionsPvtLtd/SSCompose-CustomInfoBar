package com.simform.sscustominfobar.utils

import androidx.compose.ui.text.AnnotatedString

/**
 * Wrapper class around all ways to show text in Text composable.
 * e.g [String] and [AnnotatedString]
 */
sealed interface TextType {
    class Str(val str: String) : TextType

    class AnnotatedStr(val annotatedStr: AnnotatedString) : TextType
}

fun String.toTextType() = TextType.Str(this)
fun AnnotatedString.toTextType() = TextType.AnnotatedStr(this)