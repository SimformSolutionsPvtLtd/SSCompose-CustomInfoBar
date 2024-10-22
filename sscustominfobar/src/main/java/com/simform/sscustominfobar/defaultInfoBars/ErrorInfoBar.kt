package com.simform.sscustominfobar.defaultInfoBars

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarColors
import com.simform.sscustominfobar.main.SSComposeInfoBarData
import com.simform.sscustominfobar.main.SSComposeInfoBarDefaults
import com.simform.sscustominfobar.main.toSSCustomBackground
import com.simform.sscustominfobar.res.errorRed
import com.simform.sscustominfobar.res.white

private val errorBackgroundColor = errorRed
private val errorContentColor = white

/**
 * Custom [SSComposeInfoBar] that is created to represent an error themed SSComposeInfoBar.
 *
 * @param modifier The modifier which will be applied to the [ErrorInfoBar].
 * @param errorData The [SSComposeInfoBarData] that contains the title and description of the error.
 * @param textStyle The [TextStyle] to be applied to all the text in [SSComposeInfoBar].
 * @param shape The [Shape] of the [ErrorInfoBar].
 * @param onCloseClicked Called when user clicks on the close icon in [SSComposeInfoBar].
 * @param isInfinite The flag that represents whether the duration of the [SSComposeInfoBar] is Infinite or not.
 */
@Composable
fun ErrorInfoBar(
    modifier: Modifier = Modifier,
    errorData: SSComposeInfoBarData,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = SSComposeInfoBarDefaults.shape,
    onCloseClicked: () -> Unit = {},
    isInfinite: Boolean = false
) {
    SSComposeInfoBar(
        modifier = modifier,
        title = errorData.title,
        titleStyle = textStyle,
        description = errorData.description,
        shape = shape,
        icon = errorData.icon,
        customBackground = errorBackgroundColor.toSSCustomBackground(),
        contentColors = SSComposeInfoBarColors(
            iconColor = errorContentColor,
            titleColor = errorContentColor,
            descriptionColor = errorContentColor,
            dismissIconColor = errorContentColor,
            actionButtonColors = ButtonDefaults.elevatedButtonColors()
        ),
        onCloseClicked = onCloseClicked,
        isInfinite = isInfinite
    )
}