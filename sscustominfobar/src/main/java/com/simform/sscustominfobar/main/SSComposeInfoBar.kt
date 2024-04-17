package com.simform.sscustominfobar.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.simform.sscustominfobar.main.components.CustomText
import com.simform.sscustominfobar.res.Dimens.DpMedium
import com.simform.sscustominfobar.res.Dimens.DpThirtyFour
import com.simform.sscustominfobar.utils.TextType

/**
 * Max num of lines for title in [SSComposeInfoBar]
 */
private const val TITLE_MAX_LINE = 1

/**
 * Wrapper data class for elevations used in [SSComposeInfoBar].
 *
 * @property tonalElevation
 * @property shadowElevation
 */
data class SSComposeInfoBarElevation(
    val tonalElevation: Dp,
    val shadowElevation: Dp
)

/**
 * Wrapper data class for colors used in [SSComposeInfoBar].
 *
 * @property iconColor
 * @property titleColor
 * @property descriptionColor
 */
data class SSComposeInfoBarColors(
    val iconColor: Color,
    val titleColor: Color,
    val descriptionColor: Color,
    val dismissIconColor: Color
)

/**
 * Compose Info Bar with an icon, title, and a description.
 *
 * @param modifier  The [modifier] to be applied to the [SSComposeInfoBar].
 * @param title The text to be shown in the [SSComposeInfoBar] as title.
 * @param titleStyle The [TextStyle] that will be applied to the title of the [SSComposeInfoBar].
 * @param description The text to be shown in the [SSComposeInfoBar] as Description.
 * @param descriptionStyle The [TextStyle] that will be applied to the description of the [SSComposeInfoBar].
 * @param icon The [ImageVector] that will be displayed along with the title and description.
 * @param shape The [Shape] that will be applied to the [SSComposeInfoBar].
 * @param elevations The [SSComposeInfoBarElevation] that represents the tonal and shadow elevation used in [SSComposeInfoBar].
 * @param contentColors The [SSComposeInfoBarColors] that represents the container and content color used in [SSComposeInfoBar].
 * @param contentPadding The [PaddingValues] that will be used to give padding to the contents of the [SSComposeInfoBar].
 * @param height The Height of the [SSComposeInfoBar].
 * @param onCloseClicked Called when the user clicks the clear button on [SSComposeInfoBar].
 * @param isInfinite The flag that denotes whether the [SSComposeInfoBar]'s Display Duration is infinite or not, based on this flag the clear button will be shown and hidden.
 */
@Composable
fun SSComposeInfoBar(
    modifier: Modifier = Modifier,
    title: TextType,
    titleStyle: TextStyle = SSComposeInfoBarDefaults.defaultTitleStyle,
    description: TextType? = null,
    descriptionStyle: TextStyle = SSComposeInfoBarDefaults.defaultDescriptionStyle,
    customBackground: SSCustomBackground = SSComposeInfoBarDefaults.defaultSSCustomBackground,
    icon: ImageVector = Icons.Default.Info,
    shape: Shape = SSComposeInfoBarDefaults.shape,
    elevations: SSComposeInfoBarElevation = SSComposeInfoBarDefaults.elevations,
    contentColors: SSComposeInfoBarColors = SSComposeInfoBarDefaults.colors,
    contentPadding: PaddingValues = SSComposeInfoBarDefaults.contentPadding,
    height: Dp = SSComposeInfoBarDefaults.defaultHeight,
    onCloseClicked: () -> Unit = {},
    isInfinite: Boolean = false
) {
    Surface(
        modifier = modifier
            .then(
                // NOTE: here this default modifier is added inside of then because we want to override any width and height related modifier given from outside
                Modifier
                    .fillMaxWidth()
                    .height(height)
            ),
        shape = shape,
        color = if (customBackground is SSCustomBackground.SolidColor) {
            customBackground.color
        } else {
            MaterialTheme.colorScheme.surface
        },
        tonalElevation = elevations.tonalElevation,
        shadowElevation = elevations.shadowElevation
    ) {
        Row(
            modifier = when (customBackground) {
                is SSCustomBackground.DrawableBackground -> {
                    Modifier
                        .paint(
                            painter = customBackground.image,
                            contentScale = ContentScale.Crop
                        )
                        .fillMaxWidth()
                        .padding(contentPadding)
                }

                is SSCustomBackground.GradientBrush -> {
                    Modifier
                        .background(customBackground.gradientBrush)
                        .fillMaxWidth()
                        .padding(contentPadding)
                }

                else -> {
                    Modifier
                        .fillMaxWidth()
                        .padding(contentPadding)
                }
            },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(DpThirtyFour),
                tint = contentColors.iconColor
            )
            Column(
                modifier = Modifier
                    .padding(horizontal = DpMedium)
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                CustomText(
                    text = title,
                    maxLines = TITLE_MAX_LINE,
                    overflow = TextOverflow.Ellipsis,
                    style = titleStyle,
                    color = contentColors.titleColor
                )
                if (description != null) {
                    CustomText(
                        text = description,
                        style = descriptionStyle,
                        maxLines = SSComposeInfoBarDefaults.ssInfoBarDescription,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColors.descriptionColor
                    )
                }
            }
            if (isInfinite) {
                IconButton(onClick = onCloseClicked) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        tint = contentColors.dismissIconColor
                    )
                }
            }
        }
    }
}