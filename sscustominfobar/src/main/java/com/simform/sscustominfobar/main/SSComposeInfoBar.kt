package com.simform.sscustominfobar.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.simform.sscustominfobar.main.components.CustomText
import com.simform.sscustominfobar.res.Dimens.DpFortyEight
import com.simform.sscustominfobar.res.Dimens.DpMedium
import com.simform.sscustominfobar.res.Dimens.DpSixtyFour
import com.simform.sscustominfobar.res.Dimens.DpSmall
import com.simform.sscustominfobar.res.Dimens.DpThirtyFour
import com.simform.sscustominfobar.res.Dimens.SpMedium
import com.simform.sscustominfobar.utils.TextType
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

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
 * Enum class for [SSComposeInfoBar] drag anchors to be used for Slide to Perform Action.
 */
enum class DragAnchors {
    Start,
    End,
}

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
 * @param actionText The text of the Action in the [SSComposeInfoBar].
 * @param onActionClicked Called when the user clicks the action button.
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
    actionText: String = SSComposeInfoBarDefaults.defaultActionTitle,
    onActionClicked: (() -> Unit)? = null,
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
                    maxLines = SSComposeInfoBarDefaults.titleMaxLine,
                    overflow = TextOverflow.Ellipsis,
                    style = titleStyle,
                    color = contentColors.titleColor
                )
                if (description != null) {
                    CustomText(
                        text = description,
                        style = descriptionStyle,
                        maxLines = SSComposeInfoBarDefaults.descriptionMaxLine,
                        overflow = TextOverflow.Ellipsis,
                        color = contentColors.descriptionColor
                    )
                }
            }
            if (onActionClicked != null) {
                ElevatedButton(onClick = onActionClicked) {
                    Text(text = actionText)
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

/**
 * Slide to perform action defaults
 *
 * Contains all the default values for slide to perform action info bar's properties.
 */
internal object SlideToPerformActionDefaults {
    val defaultSliderIcon = Icons.Rounded.ChevronRight
    val defaultActionTextStyle
        @Composable get() = MaterialTheme.typography.titleMedium.copy(
            fontSize = SpMedium
        )
    val defaultDoneTitleTextStyle
        @Composable get() = MaterialTheme.typography.titleMedium.copy(
            fontSize = SpMedium,
            color = Color.White
        )
    val defaultBackground @Composable get() = MaterialTheme.colorScheme.primary.toSSCustomBackground()
    val defaultBackgroundShape @Composable get() = MaterialTheme.shapes.medium
    val defaultSliderShape @Composable get() = MaterialTheme.shapes.medium
    val defaultElevations = SSComposeInfoBarDefaults.elevations
    val defaultContentPadding = PaddingValues(DpSmall)
    val defaultSliderIconColor @Composable get() = MaterialTheme.colorScheme.inversePrimary
    val defaultSliderBackgroundColor = Color.White
    val defaultLoadingBackgroundColor @Composable get() = MaterialTheme.colorScheme.inversePrimary

    val defaultInfoBarHeight = DpSixtyFour
    val defaultMarginTop = DpSmall
    val defaultSliderSize = DpFortyEight
    val defaultSlideIconSize = DpFortyEight
    val defaultVelocityThreshold = 1000
    val defaultDelayBeforeDismiss = 1200L
    val defaultDelayBeforeSliderDisappers = 100
    val defaultBorderStroke = BorderStroke(1.dp, Color.White)
    val defaultActionTitleMaxLines = 1
    val defaultDoneTitleMaxLines = 1
}

/**
 * Slide to perform action [SSComposeInfoBar]
 *
 * @param actionText The text of the Action in the [SSComposeInfoBar].
 * @param onActionDoneText The text to be shown in the [SSComposeInfoBar] when action is performed.
 * @param modifier  The [modifier] to be applied to the [SSComposeInfoBar].
 * @param sliderIcon The [ImageVector] that will be displayed inside of slider.
 * @param actionTextStyle The [TextStyle] that will be applied to the action text.
 * @param customBackground The [SSCustomBackground] that will be applied to the [SSComposeInfoBar].
 * @param borderStroke The [BorderStroke] that will be applied to the [SSComposeInfoBar].
 * @param backgroundShape The [Shape] that will be applied to the background of the [SSComposeInfoBar].
 * @param sliderShape The [Shape] that will be applied to the background of the slider.
 * @param elevations The [SSComposeInfoBarElevation] that represents the tonal and shadow elevation used in [SSComposeInfoBar].
 * @param contentPadding The [PaddingValues] that will be used to give padding to the contents of the [SSComposeInfoBar].
 * @param sliderIconTintColor The [Color] that will be applied to the slider icon.
 * @param sliderBackgroundColor The [Color] that will be applied to the background of the slider.
 * @param loadingBackgroundColor The [Color] that will be applied to the background of the loading indicator.
 * @param infoBarHeight The Height of the [SSComposeInfoBar].
 * @param verticalMargin The vertical margin of the [SSComposeInfoBar].
 * @param sliderSize The size of the slider.
 * @param sliderIconSize The size of the slider icon.
 * @param onSlideComplete Called when the user completes the slide to perform action.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SSComposeInfoBar(
    actionText: TextType,
    onActionDoneText: TextType,
    modifier: Modifier = Modifier,
    sliderIcon: ImageVector = SlideToPerformActionDefaults.defaultSliderIcon,
    actionTextStyle: TextStyle = SlideToPerformActionDefaults.defaultActionTextStyle,
    customBackground: SSCustomBackground = SlideToPerformActionDefaults.defaultBackground,
    borderStroke: BorderStroke = SlideToPerformActionDefaults.defaultBorderStroke,
    backgroundShape: Shape = SlideToPerformActionDefaults.defaultBackgroundShape,
    sliderShape: Shape = SlideToPerformActionDefaults.defaultSliderShape,
    elevations: SSComposeInfoBarElevation = SlideToPerformActionDefaults.defaultElevations,
    contentPadding: PaddingValues = SlideToPerformActionDefaults.defaultContentPadding,
    sliderIconTintColor: Color = SlideToPerformActionDefaults.defaultSliderIconColor,
    sliderBackgroundColor: Color = SlideToPerformActionDefaults.defaultSliderBackgroundColor,
    loadingBackgroundColor: Color = SlideToPerformActionDefaults.defaultLoadingBackgroundColor,
    infoBarHeight: Dp = SlideToPerformActionDefaults.defaultInfoBarHeight,
    verticalMargin: Dp = SlideToPerformActionDefaults.defaultMarginTop,
    sliderSize: Dp = SlideToPerformActionDefaults.defaultSliderSize,
    sliderIconSize: Dp = SlideToPerformActionDefaults.defaultSlideIconSize,
    onSlideComplete: () -> Unit = {}
) {
    val density = LocalDensity.current
    val slideWidth = LocalConfiguration.current.screenWidthDp.dp - sliderSize -
            contentPadding.calculateEndPadding(LocalLayoutDirection.current) -
            contentPadding.calculateEndPadding(LocalLayoutDirection.current)
    val widthInPx = with(density) {
        slideWidth.toPx()
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = { distance: Float -> distance / 2 },
            velocityThreshold = { with(density) { SlideToPerformActionDefaults.defaultVelocityThreshold.dp.toPx() } },
            animationSpec = tween()
        ).apply {
            updateAnchors(
                DraggableAnchors {
                    DragAnchors.Start at 0f
                    DragAnchors.End at widthInPx
                }
            )
        }
    }

    var isSlidingComplete by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = state.currentValue) {
        if (state.currentValue == state.targetValue && state.currentValue == DragAnchors.End) {
            isSlidingComplete = true
            delay(SlideToPerformActionDefaults.defaultDelayBeforeDismiss)
            onSlideComplete()
        }
    }

    Surface(
        modifier = modifier
            .then(
                // NOTE: here this default modifier is added inside of then because we want to override any width and height related modifier given from outside
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = verticalMargin)
                    .height(infoBarHeight)
            ),
        shape = backgroundShape,
        color = if (customBackground is SSCustomBackground.SolidColor) {
            customBackground.color
        } else {
            MaterialTheme.colorScheme.surface
        },
        tonalElevation = elevations.tonalElevation,
        shadowElevation = elevations.shadowElevation,
        border = borderStroke
    ) {
        Box(
            modifier = when (customBackground) {
                is SSCustomBackground.DrawableBackground -> {
                    Modifier
                        .paint(
                            painter = customBackground.image,
                            contentScale = ContentScale.Crop
                        )
                        .fillMaxWidth()
                }

                is SSCustomBackground.GradientBrush -> {
                    Modifier
                        .background(customBackground.gradientBrush)
                        .fillMaxWidth()
                }

                else -> {
                    Modifier
                        .fillMaxWidth()
                }
            }
        ) {
            CustomText(
                text = actionText,
                modifier = Modifier
                    .align(Alignment.Center),
                style = actionTextStyle,
                maxLines = SlideToPerformActionDefaults.defaultActionTitleMaxLines
            )
            LoadingBackground(
                modifier = Modifier
                    .height(infoBarHeight)
                    .width(with(density) {
                        // Here height is also used as initial width since we want a circle.
                        infoBarHeight + state
                            .requireOffset()
                            .toDp()
                    })
                    .clip(backgroundShape)
                    .background(loadingBackgroundColor)
            )
            AnimatedVisibility(!isSlidingComplete, exit = scaleOut() + fadeOut()) {
                SwipeIndicator(
                    modifier = Modifier
                        .padding(contentPadding)
                        .size(sliderSize)
                        .offset {
                            IntOffset(
                                x = state
                                    .requireOffset()
                                    .roundToInt(),
                                y = 0
                            )
                        }
                        .anchoredDraggable(state, Orientation.Horizontal)
                        .clip(sliderShape),
                    sliderIcon = sliderIcon,
                    sliderIconSize = sliderIconSize,
                    sliderIconTintColor = sliderIconTintColor,
                    backgroundColor = sliderBackgroundColor
                )
            }
            AnimatedVisibility(
                visible = isSlidingComplete,
                modifier = Modifier.align(Alignment.Center),
                enter = fadeIn(tween(delayMillis = SlideToPerformActionDefaults.defaultDelayBeforeSliderDisappers))
            ) {
                CustomText(
                    text = onActionDoneText,
                    modifier = Modifier
                        .align(Alignment.Center),
                    style = SlideToPerformActionDefaults.defaultDoneTitleTextStyle,
                    maxLines = SlideToPerformActionDefaults.defaultDoneTitleMaxLines
                )
            }
        }
    }
}

@Composable
internal fun SwipeIndicator(
    modifier: Modifier = Modifier,
    sliderIcon: ImageVector,
    sliderIconSize: Dp,
    sliderIconTintColor: Color,
    backgroundColor: Color
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.background(backgroundColor)
    ) {
        Icon(
            imageVector = sliderIcon,
            contentDescription = null,
            tint = sliderIconTintColor,
            modifier = Modifier.size(sliderIconSize)
        )
    }
}

@Composable
fun LoadingBackground(
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {}
}