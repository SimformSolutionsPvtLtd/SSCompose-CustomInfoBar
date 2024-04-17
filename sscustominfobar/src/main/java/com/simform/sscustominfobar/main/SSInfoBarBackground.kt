package com.simform.sscustominfobar.main

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

/**
 * Sealed Interface of supported background types in [SSComposeInfoBar].
 */
sealed interface SSCustomBackground {
    class SolidColor(val color: Color) : SSCustomBackground

    class GradientBrush(val gradientBrush: Brush) : SSCustomBackground

    class DrawableBackground(val image: Painter) : SSCustomBackground
}

/**
 * Extension method to convert [Color] to [SSCustomBackground].
 */
fun Color.toSSCustomBackground() = SSCustomBackground.SolidColor(this)

/**
 * Extension method to convert [Brush] to [SSCustomBackground].
 */
fun Brush.toSSCustomBackground() = SSCustomBackground.GradientBrush(this)

/**
 * Extension method to convert [Painter] to [SSCustomBackground].
 */
fun Painter.toSSCustomBackground() = SSCustomBackground.DrawableBackground(this)