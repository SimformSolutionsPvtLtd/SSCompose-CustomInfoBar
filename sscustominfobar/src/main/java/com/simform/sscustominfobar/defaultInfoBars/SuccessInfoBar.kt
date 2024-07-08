package com.simform.sscustominfobar.defaultInfoBars

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
import com.simform.sscustominfobar.res.successGreen
import com.simform.sscustominfobar.res.white

private val successBackgroundColor = successGreen
private val successContentColor = white

/**
 * Custom [SSComposeInfoBar] that is created to represent an success themed SSComposeInfoBar.
 *
 * @param modifier The modifier which will be applied to the [SuccessInfoBar].
 * @param successData The [SSComposeInfoBarData] that contains the title and description of the success.
 * @param textStyle The [TextStyle] to be applied to all the text in [SSComposeInfoBar].
 * @param shape The [Shape] of the [SuccessInfoBar].
 * @param onCloseClicked Called when user clicks on the close icon in [SSComposeInfoBar].
 * @param isInfinite The flag that represents whether the duration of the [SSComposeInfoBar] is Infinite or not.
 */
@Composable
fun SuccessInfoBar(
    modifier: Modifier = Modifier,
    successData: SSComposeInfoBarData,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = SSComposeInfoBarDefaults.shape,
    onCloseClicked: () -> Unit = {},
    isInfinite: Boolean = false
) {
    SSComposeInfoBar(
        modifier = modifier,
        title = successData.title,
        titleStyle = textStyle,
        description = successData.description,
        shape = shape,
        icon = successData.icon,
        customBackground = successBackgroundColor.toSSCustomBackground(),
        contentColors = SSComposeInfoBarColors(
            iconColor = successContentColor,
            titleColor = successContentColor,
            descriptionColor = successContentColor,
            dismissIconColor = successContentColor
        ),
        onCloseClicked = onCloseClicked,
        isInfinite = isInfinite
    )
}