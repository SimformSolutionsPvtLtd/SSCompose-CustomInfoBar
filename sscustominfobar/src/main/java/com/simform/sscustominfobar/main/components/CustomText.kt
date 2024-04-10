package com.simform.sscustominfobar.main.components

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.utils.TextType

/**
 * CustomText composable to use inside [SSComposeInfoBar] to show either normal string or an
 * annotated string.
 *
 * @param text of type [TextType].
 * @param modifier modifier which should be applied to CustomText.
 * @param style [TextStyle] for the text.
 * @param color [Color] of the text.
 * @param textAlign [TextAlign] of text.
 * @param overflow TextOverFlow.
 * @param maxLines maxLines for the text to display.
 */
@Composable
internal fun CustomText(
    text: TextType,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Clip,
    maxLines: Int
) {
    when (text) {
        is TextType.AnnotatedStr -> {
            Text(
                text = text.annotatedStr,
                color = color,
                modifier = modifier,
                style = style,
                textAlign = textAlign,
                overflow = overflow,
                maxLines = maxLines
            )
        }

        is TextType.Str -> {
            Text(
                text = text.str,
                color = color,
                modifier = modifier,
                style = style,
                textAlign = textAlign,
                overflow = overflow,
                maxLines = maxLines
            )
        }
    }
}