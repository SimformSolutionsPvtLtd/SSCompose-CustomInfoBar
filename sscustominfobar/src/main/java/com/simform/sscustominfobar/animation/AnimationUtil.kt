package com.simform.sscustominfobar.animation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import com.simform.sscustominfobar.R
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDefaults
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection

private const val DefaultAnimationDuration = 600
private const val DefaultHidingAnimationDuration = 300

/**
 * Internal function to get enter animation of compose info bar based on [SSComposeInfoBarDirection] provided
 *
 * Example: If the animation direction is from top to bottom then we will get slideInVertically with initial offset from top
 * and if the animation direction is from bottom to top then the initial offset will be set to outside of bottom of the screen
 * @param direction direction from which [SSComposeInfoBar] comes in and exits.
 * @param duration duration of animation in milliseconds
 */
internal fun getEnterAnimation(
    direction: SSComposeInfoBarDirection,
    duration: Int = DefaultAnimationDuration
) =
    slideInVertically(tween(duration), initialOffsetY = { height ->
        if (direction == SSComposeInfoBarDirection.Top) {
            -height
        } else {
            height
        }
    })

/**
 * Internal function to get exit animation of compose info bar based on [SSComposeInfoBarDirection] provided
 *
 * Example: If the animation direction is from top then we will get slideInVertically with target offset from top
 * and if the animation direction is from bottom then the target offset will be set to outside of bottom of the screen
 * @param direction direction from which [SSComposeInfoBar] comes in and exits.
 * @param duration duration of animation in milliseconds
 */
internal fun getExitAnimation(
    direction: SSComposeInfoBarDirection,
    duration: Int = DefaultAnimationDuration
) =
    slideOutVertically(tween(duration), targetOffsetY = { height ->
        if (direction == SSComposeInfoBarDirection.Top) {
            -height
        } else {
            height
        }
    })

/**
 * An animation utility method that returns animated yOffset for [SSComposeInfoBar] when
 * scroll to hide and show is enabled.
 *
 * @param shouldBeVisible flag that denoted whether the [SSComposeInfoBar] should be visible or not.
 * @param direction [SSComposeInfoBarDirection] to show and hide [SSComposeInfoBar] correctly.
 * @param duration Animation duration in milliseconds, default is set to 300 ms.
 * @return [State] object of type float that can be observed.
 */
@Composable
fun getAnimatedOffset(
    shouldBeVisible: Boolean,
    direction: SSComposeInfoBarDirection,
    duration: Int = DefaultHidingAnimationDuration
): State<Float> {
    val density = LocalDensity.current
    val shownOffset = 0f
    val hiddenOffset =
        if (direction == SSComposeInfoBarDirection.Top) with(density) { -SSComposeInfoBarDefaults.defaultHeight.toPx() }
        else with(density) { SSComposeInfoBarDefaults.defaultHeight.toPx() }
    return animateFloatAsState(
        targetValue = if (shouldBeVisible) shownOffset else hiddenOffset,
        animationSpec = tween(duration),
        label = stringResource(R.string.scroll_to_hide_animation)
    )
}