package com.simform.sscustominfobar.defaultInfoBars

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDefaults
import com.simform.sscustominfobar.utils.TextType

/**
 * Demo Slide to perform action [SSComposeInfoBar]
 *
 * @param actionText The text of the Action in the [SSComposeInfoBar].
 * @param onActionDoneText The text to be shown in the [SSComposeInfoBar] when action is performed.
 * @param modifier  The [modifier] to be applied to the [SSComposeInfoBar].
 * @param sliderIcon The [ImageVector] that will be displayed inside of slider.
 * @param backgroundShape The [Shape] of the background of the [SSComposeInfoBar].
 * @param sliderShape The [Shape] of the slider.
 * @param onSlideComplete The lambda block to be executed when the sliding is completed.
 */
@Composable
fun SlideToPerformInfoBar(
    actionText: TextType,
    onActionDoneText: TextType,
    modifier: Modifier = Modifier,
    sliderIcon: ImageVector = Icons.Rounded.ChevronRight,
    backgroundShape: Shape = CircleShape,
    sliderShape: Shape = CircleShape,
    onSlideComplete: () -> Unit = {}
) {
    SSComposeInfoBar(
        actionText = actionText,
        onActionDoneText = onActionDoneText,
        sliderIcon = sliderIcon,
        modifier = modifier,
        backgroundShape = backgroundShape,
        sliderShape = sliderShape,
        onSlideComplete = onSlideComplete,
        contentPadding = PaddingValues(SSComposeInfoBarDefaults.contentPadding.calculateTopPadding())
    )
}