package com.simform.sscustominfobar.defaultInfoBars

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarColors
import com.simform.sscustominfobar.main.SSComposeInfoBarData
import com.simform.sscustominfobar.main.SSComposeInfoBarDefaults
import com.simform.sscustominfobar.main.toSSCustomBackground
import com.simform.sscustominfobar.res.warningOrange
import com.simform.sscustominfobar.res.white

private val warningBackgroundColor = warningOrange
private val warningContentColor = white

/**
 * Custom [SSComposeInfoBar] that is created to represent an error themed SSComposeInfoBar.
 *
 * @param modifier The modifier which will be applied to the [WarningInfoBar].
 * @param warningData The [SSComposeInfoBarData] that contains the title and description of the warning.
 * @param icon The [ImageVector] that will be shown in [SSComposeInfoBar] for warning.
 * @param textStyle The [TextStyle] to be applied to all the text in [SSComposeInfoBar].
 * @param shape The [Shape] of the [WarningInfoBar].
 * @param onCloseClicked Called when user clicks on the close icon in [SSComposeInfoBar].
 * @param isInfinite The flag that represents whether the duration of the [SSComposeInfoBar] is Infinite or not.
 */
@Composable
fun WarningInfoBar(
    modifier: Modifier = Modifier,
    warningData: SSComposeInfoBarData,
    icon: ImageVector = Icons.Outlined.Warning,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = SSComposeInfoBarDefaults.shape,
    onCloseClicked: () -> Unit = {},
    isInfinite: Boolean = false
) {
    SSComposeInfoBar(
        modifier = modifier,
        title = warningData.title,
        titleStyle = textStyle,
        description = warningData.description,
        shape = shape,
        icon = icon,
        customBackground = warningBackgroundColor.toSSCustomBackground(),
        contentColors = SSComposeInfoBarColors(
            iconColor = warningContentColor,
            titleColor = warningContentColor,
            descriptionColor = warningContentColor,
            dismissIconColor = warningContentColor
        ),
        onCloseClicked = onCloseClicked,
        isInfinite = isInfinite
    )
}