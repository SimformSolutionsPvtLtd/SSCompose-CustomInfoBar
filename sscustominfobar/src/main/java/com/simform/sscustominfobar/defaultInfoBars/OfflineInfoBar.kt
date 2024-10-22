package com.simform.sscustominfobar.defaultInfoBars

import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarData

/**
 * Custom [SSComposeInfoBar] that is created to represent an offline themed SSComposeInfoBar.
 *
 * @param modifier The modifier which will be applied to the [OfflineInfoBar].
 * @param offlineData The [SSComposeInfoBarData] that contains the title and description of the offlineInfoBar.
 * @param textStyle The [TextStyle] to be applied to all the text in [SSComposeInfoBar].
 * @param shape The [Shape] of the [OfflineInfoBar].
 */
@Composable
fun OfflineInfoBar(
    modifier: Modifier = Modifier,
    offlineData: SSComposeInfoBarData,
    textStyle: TextStyle = LocalTextStyle.current,
    shape: Shape = RectangleShape
) {
    SSComposeInfoBar(
        modifier = modifier,
        title = offlineData.title,
        titleStyle = textStyle,
        description = offlineData.description,
        shape = shape,
        icon = offlineData.icon
    )
}