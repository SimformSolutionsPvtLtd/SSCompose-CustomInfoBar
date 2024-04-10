package com.simform.sscustominfobar.animation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import com.simform.sscustominfobar.main.SSComposeInfoBar
import com.simform.sscustominfobar.main.SSComposeInfoBarDirection

private const val DefaultAnimationDuration = 600

/**
 * Internal function to get enter animation of jet info bar based on [SSComposeInfoBarDirection] provided
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
 * Internal function to get exit animation of jet info bar based on [SSComposeInfoBarDirection] provided
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